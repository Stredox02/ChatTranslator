package it.stredox02.chattranslator.nms.v1_19_R1;

import io.netty.channel.*;
import it.stredox02.chattranslator.common.data.PlayerMessage;
import it.stredox02.chattranslator.common.managers.PlayerMessagesManager;
import it.stredox02.chattranslator.common.managers.TranslatorManager;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import it.stredox02.chattranslator.nms.interfaces.NMSManager;
import net.minecraft.network.chat.ChatMessageContent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Level;

public class NmsManagerImpl implements NMSManager {

    private final ChatTranslatorPlugin enhancedTranslator;
    private final TranslatorManager translatorManager;

    public NmsManagerImpl(ChatTranslatorPlugin enhancedTranslator){
        this.enhancedTranslator = enhancedTranslator;

        translatorManager = new TranslatorManager(enhancedTranslator);
    }

    @Override
    public void addPlayer(@NotNull Player player) {
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        ChannelPipeline addedHandler = pipeline.addBefore("packet_handler", NMSManager.HANDLER_NAME, new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);

                if (packet instanceof ServerboundChatPacket chat) {
                    String message = chat.message();
                    if (message.startsWith("/")) {
                        return;
                    }
                    PlayerMessagesManager.getInstance().addPlayerMessage(new PlayerMessage(player.getUniqueId(), message));
                }
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (interceptPacket(channelHandlerContext, packet, channelPromise)) {
                    return;
                }

                super.write(channelHandlerContext, packet, channelPromise);
            }

            public boolean interceptPacket(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) {
                if (!(packet instanceof ClientboundPlayerChatPacket chat)) {
                    return false;
                }
                String messageToTranslate = chat.message().signedContent().plain();

                Component signedComponent = chat.message().unsignedContent().orElse(null);
                if(signedComponent != null){
                    messageToTranslate = CraftChatMessage.fromComponent(signedComponent);
                }

                PlayerMessage message = PlayerMessagesManager.getInstance().getMessage(messageToTranslate);
                if (message == null) { //This will block all unwritten messages from players
                    return false;
                }

                String finalMessageToTranslate = messageToTranslate;
                translatorManager.translate(player, message.getMessage(), result -> {
                    message.getPlayerConsumed().add(player.getUniqueId());
                    if (message.getPlayerConsumed().size() == Bukkit.getOnlinePlayers().size()) {
                        //When everyone consumes the message I can remove it
                        PlayerMessagesManager.getInstance().removeMessage(message);
                    }

                    String effectiveResult = StringUtils.replace(finalMessageToTranslate, message.getMessage(), result);

                    //Send the "log" message
                    enhancedTranslator.log(Level.INFO,effectiveResult);

                    //Send again the message
                    SignedMessageBody signedMessageBody = new SignedMessageBody(
                            new ChatMessageContent(result),
                            chat.message().timeStamp(),
                            chat.message().salt(),
                            chat.message().signedBody().lastSeen());

                    ClientboundPlayerChatPacket chatPacket = new ClientboundPlayerChatPacket(
                            new PlayerChatMessage(chat.message().signedHeader(),
                                    chat.message().headerSignature(),
                                    signedMessageBody,
                                    Optional.ofNullable(CraftChatMessage.fromStringOrNull(effectiveResult)),
                                    chat.message().filterMask()),
                            chat.chatType());

                    try {
                        super.write(channelHandlerContext, chatPacket, channelPromise);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return true;
            }
        });

        if(enhancedTranslator.isDebug()){
            if(addedHandler != null){
                enhancedTranslator.log(Level.INFO,"&aSuccessfully injected packet handler to " + player.getName());
            } else {
                enhancedTranslator.log(Level.INFO,"&cUnable to inject the packet handler to " + player.getName());
            }
        }
    }

    @Override
    public void removePlayer(@NotNull Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(NMSManager.HANDLER_NAME);

            enhancedTranslator.log(Level.INFO, "&aSuccessfully removed packet handler from " + player.getName());
            return null;
        });
    }

}
