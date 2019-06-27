package me.bot.gifs;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

public class Gifmanager {

	private String name;
	private String[] gifs;
	private int count;
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
		channel.createMessage(spec -> spec.setEmbed(specConsumer(image, executor, username))).subscribe();
		return image;
	}

	private Consumer<EmbedCreateSpec> specConsumer(String image, String executor, String username) {
		return spec ->
			spec.setColor(new Color(511973))
			.setImage(image)
			.setTitle(replacer.replace("%user",username).replace("%executor",executor));
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
