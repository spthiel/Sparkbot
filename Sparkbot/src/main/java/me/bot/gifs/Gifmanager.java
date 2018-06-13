package me.bot.gifs;

import discord4j.core.object.entity.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;

import java.util.Random;

public class Gifmanager {

	private String name;
	private String[] gifs;
	private String replacer;
	private static Random random = new Random();

	public Gifmanager(String name, String[] gifs, String replacer) {
		this.name = name;
		this.gifs = gifs;
		this.replacer = replacer;
	}

	public void run(TextChannel channel, String executor, String username) {
		String image = getRandomImage();
		EmbedCreateSpec embed = new EmbedCreateSpec();
		embed.setColor(511973);
		embed.setImage(image);
		embed.setTitle(replacer.replace("%user",username).replace("%executor",executor));
		channel.createMessage(new MessageCreateSpec().setEmbed(embed)).subscribe();
	}

	private String getRandomImage() {
		int rnd = random.nextInt(gifs.length);
		return gifs[rnd];
	}

	public String getName() {
		return name;
	}
}
