package me.bot.gifs;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.HashMap;
import java.util.Random;

public class GifManager {

	private final String   name;
	private final String[] gifs;
	private final int      count;
	private final String   replacer;
	private static final Random random = new Random();
	
	private static final HashMap<Snowflake, Integer> sent = new HashMap<>();

	public GifManager(String name, String[] gifs, String replacer) {
		this.name = name;
		this.gifs = gifs;
		count = gifs.length;
		this.replacer = replacer;
	}

	public String run(TextChannel channel, String executor, String username) {
		sent.compute(channel.getId(), (key, value) -> value == null ? random.nextInt(100) : value+1);
		String image = getImage(channel.getId());
		channel.createMessage(specConsumer(image, executor, username)).subscribe();
		return image;
	}

	private EmbedCreateSpec specConsumer(String image, String executor, String username) {
		return EmbedCreateSpec.builder()
			.color(Color.of(511973))
			.image(image)
			.title(replacer.replace("%user",username).replace("%executor",executor)).build();
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
