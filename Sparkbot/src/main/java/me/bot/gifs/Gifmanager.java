package me.bot.gifs;

import discord4j.core.object.entity.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;

import java.awt.*;
import java.util.Random;

public class Gifmanager {

	private String name;
	private String[] gifs;
	private int count = 0;
	private String replacer;
	private static Random random = new Random();

	public Gifmanager(String name, String[] gifs, String replacer) {
		this.name = name;
		this.gifs = gifs;
		count = gifs.length;
		this.replacer = replacer;
	}

	public String run(TextChannel channel, String executor, String username) {
		String image = getRandomImage();
		EmbedCreateSpec embed = new EmbedCreateSpec();
		embed.setColor(new Color(511973));
		embed.setImage(image);
		embed.setTitle(replacer.replace("%user",username).replace("%executor",executor));
		channel.createMessage(new MessageCreateSpec().setEmbed(embed)).subscribe();
		return image;
	}

	private String getRandomImage() {
		int rnd = random.nextInt(gifs.length);
		return gifs[rnd];
	}

	public int length() {
		return count;
	}

	public String getName() {
		return name;
	}
}
