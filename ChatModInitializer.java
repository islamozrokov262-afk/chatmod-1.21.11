package ru.qwalsss.chatmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import java.util.concurrent.ThreadLocalRandom;

public class ChatModInitializer implements ModInitializer {
    
    // Твой золотой запас ХвХ-смайликов + сочные дописки от меня!
    private static final String[] SMILES = {
        " О_о", " 0_0", " UwU", " YwY", " xD", " ;)", " ;(", " :0", " :\\", 
        " .QoQ.", " n_n", " r_r", " p_q", " q_p", " u_u", " @.@"
    };

    @Override
    public void onInitialize() {
        ClientSendMessageEvents.ALLOW_CHAT_MESSAGE.register((message) -> {
            String trimmed = message.trim();
            
            // Базовая проверка на пустоту
            if (trimmed.isEmpty()) {
                return true;
            }

            // Защита от дублирования: проверяем, не заканчивается ли сообщение уже на какой-то смайл
            for (String smile : SMILES) {
                if (trimmed.endsWith(smile.trim())) {
                    return true;
                }
            }

            // Проверка первого символа
            char firstChar = trimmed.charAt(0);
            
            // Если ты пишешь команду сервера (/ или .), мод полностью засыпает и не мешает
            if (firstChar == '/' || firstChar == '.') {
                return true; 
            }

            // ХАКЕРСКИЙ РАНДОМ: Выбираем случайный смайл из нашего списка
            int randomIndex = ThreadLocalRandom.current().nextInt(SMILES.length);
            String chosenSmile = SMILES[randomIndex];

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.player.networkHandler != null) {
                // Склеиваем твоё сообщение со случайным смайлом
                client.player.networkHandler.sendChatMessage(message + chosenSmile);
                return false; // Глушим старое голое сообщение, чтобы не спамить
            }
            return true;
        });
    }
}
