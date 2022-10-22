package it.stredox02.chattranslator.nms.v1_8_R3;

import io.netty.channel.*;
import it.stredox02.chattranslator.common.data.PlayerMessage;
import it.stredox02.chattranslator.common.managers.PlayerMessagesManager;
import it.stredox02.chattranslator.common.managers.TranslatorManager;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import it.stredox02.chattranslator.nms.interfaces.NMSManager;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
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
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getUniqueId() + ":" + NMSManager.HANDLER_NAME, new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                if (packet instanceof PacketPlayInChat) {
                    PacketPlayInChat chat = (PacketPlayInChat) packet;
                    String message = chat.a();

                    message = StringUtils.normalizeSpace(message); // Ensure following bukkit logic

                    if(!message.startsWith("/")) {
                        PlayerMessagesManager.getInstance().addPlayerMessage(new PlayerMessage(player.getUniqueId(), message));
                    }
                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (interceptPacket(channelHandlerContext, packet, channelPromise)) {
                    return;
                }

                super.write(channelHandlerContext, packet, channelPromise);
            }

            public boolean interceptPacket(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) {
                if (!(packet instanceof PacketPlayOutChat)) {
                    return false;
                }
                PacketPlayOutChat chat = (PacketPlayOutChat) packet;

                Field chatStringField;
                String messageToTranslate;
                try {
                    chatStringField = chat.getClass().getDeclaredField("a");
                    chatStringField.setAccessible(true);

                    IChatBaseComponent chatBaseComponent = (IChatBaseComponent) chatStringField.get(chat);
                    messageToTranslate = CraftChatMessage.fromComponent(chatBaseComponent, EnumChatFormat.WHITE);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();

                    return false;
                }

                PlayerMessage message = PlayerMessagesManager.getInstance().getMessage(messageToTranslate);
                if (message == null) { //This will block all unwritten messages from players
                    return false;
                }

                String finalMessageToTranslate = messageToTranslate;
                Field finalChatStringField = chatStringField;
                translatorManager.translate(player, message.getMessage(), result -> {
                    message.getPlayerConsumed().add(player.getUniqueId());
                    if(message.getPlayerConsumed().size() == Bukkit.getOnlinePlayers().size()){
                        //When everyone consumes the message I can remove it
                        PlayerMessagesManager.getInstance().removeMessage(message);
                    }

                    String effectiveResult = StringUtils.replace(finalMessageToTranslate, message.getMessage(), result);

                    //Send the "log" message
                    enhancedTranslator.log(Level.INFO,effectiveResult);

                    try {
                        IChatBaseComponent[] modified = CraftChatMessage.fromString(effectiveResult);
                        finalChatStringField.set(chat, modified[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    //Send again the message
                    try {
                        super.write(channelHandlerContext, packet, channelPromise);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public void removePlayer(@NotNull Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> channel.pipeline().remove(player.getUniqueId() + ":" + NMSManager.HANDLER_NAME));
    }

}
