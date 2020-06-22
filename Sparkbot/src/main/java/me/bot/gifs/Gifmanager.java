package me.bot.gifs;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class Gifmanager {

	private String name;
	private String[] gifs;
	private int count;
	private String replacer;
	private static Random random = new Random();
	
	private static HashMap<Snowflake, Integer> sent = new HashMap<>();

	public Gifmanager(String name, String[] gifs, String replacer) {
		this.name = name;
		this.gifs = gifs;
		count = gifs.length;
		this.replacer = replacer;
	}

	public String run(TextChannel channel, String executor, String username) {
		sent.compute(channel.getId(), (key, value) -> value == null ? random.nextInt(100) : value+1);
		String image = getImage(channel.getId());
		channel.createMessage(spec -> spec.setEmbed(specConsumer(image, executor, username))).subscribe();
		return image;
	}

	private Consumer<EmbedCreateSpec> specConsumer(String image, String executor, String username) {
		return spec ->
			spec.setColor(Color.of(511973))
			.setImage(image)
			.setTitle(replacer.replace("%user",username).replace("%executor",executor));
	}

	private String getImage(Snowflake channel) {
		return gifs[sent.get(channel)%gifs.length];
	}

	public int length() {
		return count;
	}

	public String getName() {
		return name;
	}
}
