package com.arisnight.betterxp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterXP extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Регистрируем события
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Проверяем, что убитый моб - игрок
        if (event.getEntity() instanceof Player) {
            return;
        }

        // Получаем игрока, который убил моба
        Player player = event.getEntity().getKiller();
        if (player != null) {
            // Проверяем, что у игрока в левой руке бутылка
            ItemStack itemInHand = player.getInventory().getItemInOffHand();
            if (itemInHand.getType() == Material.GLASS_BOTTLE) {
                int exp = event.getDroppedExp();

                // Вычисляем количество бутылок опыта
                int bottlesToGive = exp / 5;

                // Удаляем пустые бутылки из инвентаря
                if (bottlesToGive > 0) {
                    itemInHand.setAmount(itemInHand.getAmount() - bottlesToGive);
                    player.getInventory().setItemInOffHand(itemInHand);

                    // Выдаем бутылки опыта
                    for (int i = 0; i < bottlesToGive; i++) {
                        player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE));
                    }

                    // Обнуляем опыт, чтобы он не поступал к игроку
                    event.setDroppedExp(0);
                }
            }
        }
    }
}