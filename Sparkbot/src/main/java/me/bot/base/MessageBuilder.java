/*
 *     This file is part of Discord4J v2.
 *
 *     Discord4J is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Discord4J is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.bot.base;


import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Used to configure and send a {@link Message}.
 */
@SuppressWarnings("unused")
public class MessageBuilder {

	/**
	 * The raw content of the message.
	 */
	private String content = "";
	/**
	 * The channel the message will be sent in.
	 */
	private MessageChannel channel;
	/**
	 * The client the message belongs to.
	 */
	private DiscordClient client;
	/**
	 * Whether the message should use text-to-speech.
	 */
	private boolean tts = false;
	/**
	 * The embed in the message.
	 */
	private EmbedCreateSpec embed;
	/**
	 * The input stream to read and attach.
	 */
	private InputStream stream;
	/**
	 * The name of the attachment that should be shown in Discord.
	 */
	private String fileName;

	private MessageCreateSpec messagespec;

	public MessageBuilder(DiscordClient client) {
		this.client = client;
	}

	/**
	 * Sets the raw content of the message.
	 *
	 * @param content The raw content of the message.
	 * @return The builder instance.
	 */
	public MessageBuilder withContent(String content) {
		this.content = "";
		return appendContent(content);
	}

	/**
	 * Sets the raw content of the message.
	 *
	 * @param content The raw content of the message.
	 * @param styles The styles to apply to the content.
	 * @return The builder instance.
	 */
	public MessageBuilder withContent(String content, Styles... styles) {
		this.content = "";
		return appendContent(content, styles);
	}

	/**
	 * Appends content to the message.
	 *
	 * @param content The content to append.
	 * @return The builder instance.
	 */
	public MessageBuilder appendContent(String content) {
		this.content += content;
		return this;
	}

	/**
	 * Appends content to the message.
	 *
	 * @param content The content to append.
	 * @param styles The styles to apply to the content.
	 * @return The builder instance.
	 */
	public MessageBuilder appendContent(String content, Styles... styles) {

		for (Styles style : styles) {
			content = style.markdown + content + style.reverseMarkdown;
		}

		this.content += content;

		return this;
	}

	/**
	 * Sets the channel the message will be sent in.
	 *
	 * @param channelID The channel the message will be sent in.
	 * @return The builder instance.
	 */
	public MessageBuilder withChannel(long channelID) {
		this.channel = DiscordUtils.getMessageChannelOfChannel(client.getChannelById(Snowflake.of(channelID)).block());
		return this;
	}

	/**
	 * Sets the channel the message will be sent in.
	 *
	 * @param channel The channel the message will be sent in.
	 * @return The builder instance.
	 */
	public MessageBuilder withChannel(MessageChannel channel) {
		this.channel = channel;
		return this;
	}

	/**
	 * Sets whether the message should use text-to-speech.
	 *
	 * @param tts Whether the message should use text-to-speech.
	 * @return The builder instance.
	 */
	public MessageBuilder withTTS(boolean tts) {
		this.tts = tts;
		return this;
	}

	/**
	 * Sets the message to use text-to-speech.
	 *
	 * @return The builder instance.
	 */
	public MessageBuilder withTTS() {
		return withTTS(true);
	}

	/**
	 * Sets the content to a multiline code block with no language highlighting.
	 *
	 * @param content The content inside the code block.
	 * @return The builder instance.
	 */
	public MessageBuilder withQuote(String content) {
		return withCode("", content);
	}

	/**
	 * Appends a multiline code block with no language highlighting to the message.
	 *
	 * @param content The content inside the code block.
	 * @return The builder instance.
	 */
	public MessageBuilder appendQuote(String content) {
		return appendCode("", content);
	}

	/**
	 * Sets the content to a multiline code block.
	 *
	 * @param language The language to syntax highlight the code block with.
	 * @param content The content inside the code block.
	 * @return The builder instance.
	 */
	public MessageBuilder withCode(String language, String content) {
		this.content = "";
		return appendCode(language, content);
	}

	/**
	 * Appends a multiline code block to the message.
	 *
	 * @param language The language to syntax highlight the code block with.
	 * @param content The content inside the code block.
	 * @return The builder instance.
	 */
	public MessageBuilder appendCode(String language, String content) {
		return appendContent(language+"\n"+content, Styles.CODE_WITH_LANG);
	}

	/**
	 * Sets the embed in the message.
	 *
	 * @param embed The embed in the message.
	 * @return The builder instance.
	 */
	public MessageBuilder withEmbed(EmbedCreateSpec embed) {
		this.embed = embed;
		return this;
	}

	/**
	 * Sets the attachment to be sent with the message.
	 *
	 * @param file The attachment to send.
	 * @return The builder instance.
	 *
	 * @throws FileNotFoundException If the file cannot be found.
	 */
	public MessageBuilder withFile(File file) throws FileNotFoundException {
		if (file == null)
			throw new NullPointerException("File argument is null");
		this.stream = new FileInputStream(file);
		this.fileName = file.getName();
		return this;
	}

	/**
	 * Sets the attachment to be sent with the message.
	 *
	 * @param fileName Name of the attachment.
	 * @param stream Input stream of the attachment.
	 * @return The builder instance.
	 *
	 */
	public MessageBuilder withAttachment(String fileName,InputStream stream) {
		if (stream == null)
			throw new NullPointerException("File argument is null");
		this.stream = stream;
		this.fileName = fileName;
		return this;
	}

	/**
	 * Sets the attachment to be sent with the message.
	 *
	 * @param fileName Name of the attachment.
	 * @param content Content of the attachment.
	 * @return The builder instance.
	 *
	 */
	public MessageBuilder withAttachment(String fileName,String content) {
		if (content == null)
			throw new NullPointerException("File argument is null");
		this.stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		this.fileName = fileName;
		return this;
	}

	/**
	 * Sets the attachment to be sent with the message.
	 *
	 * @param stream The input stream to read and attach.
	 * @param fileName The name of the attachment that should be shown in Discord.
	 * @return The builder instance.
	 */
	public MessageBuilder withFile(InputStream stream, String fileName) {
		this.stream = stream;
		this.fileName = fileName;
		return this;
	}

	/**
	 * Gets the raw content of the message.
	 *
	 * @return The The raw content of the message.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Gets the channel the message will be sent in.
	 *
	 * @return The channel the message will be sent in.
	 */
	public MessageChannel getChannel() {
		return channel;
	}

	/**
	 * Gets the embed in the message.
	 *
	 * @return The embed in the message.
	 */
	public EmbedCreateSpec getEmbedObject() {
		return embed;
	}

	/**
	 * Gets whether the message should use text-to-speech.
	 *
	 * @return Whether the message should use text-to-speech.
	 */
	public boolean isUsingTTS() {
		return tts;
	}

	/**
	 * Builds the MessageCreateSpec
	 *
	 * @return The built messagespec object.
	 */
	public MessageCreateSpec build() {
		messagespec = new MessageCreateSpec();
		messagespec.setContent(content);
		if(stream != null)
			messagespec.setFile(fileName,stream);
		if(embed != null)
			messagespec.setEmbed(embed);
		return messagespec;
	}

	/**
	 * Sends the message as configured by the builder.
	 *
	 * @return The sent message object.
	 */
	public Message send() {
		if(messagespec == null)
			build();
		return channel.createMessage(messagespec).block();
	}

	/**
	 * The Markdown formatting styles that can be used in messages.
	 */
	public enum Styles {
		ITALICS("*"),
		BOLD("**"),
		BOLD_ITALICS("***"),
		STRIKEOUT("~~"),
		CODE("```\n"),
		INLINE_CODE("`"),
		UNDERLINE("__"),
		UNDERLINE_ITALICS("__*"),
		UNDERLINE_BOLD("__**"),
		UNDERLINE_BOLD_ITALICS("__***"),
		CODE_WITH_LANG("```");

		final String markdown, reverseMarkdown;

		Styles(String markdown) {
			this.markdown = markdown;
			this.reverseMarkdown = new StringBuilder(markdown).reverse().toString();
		}

		/**
		 * Gets the markdown formatting for the style.
		 *
		 * @return The markdown formatting.
		 */
		public String getMarkdown() {
			return markdown;
		}

		/**
		 * Gets the reversed markdown formatting to be appended to the end of a formatted string.
		 *
		 * @return The reversed markdown formatting.
		 */
		public String getReverseMarkdown() {
			return reverseMarkdown;
		}

		@Override
		public String toString() {
			return markdown;
		}
	}
}