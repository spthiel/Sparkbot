package me.bot.base;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MessageBuilder {

    /**
     * The channel the message will be sent in.
     */
    private MessageChannel channel;
    /**
     * The raw content of the message.
     */
    private String         content;
    /**
     * Whether the message should use text-to-speech.
     */
    private boolean tts = false;
    /**
     * The embed in the message.
     */
    private Consumer<EmbedCreateSpec> embed;
    private Consumer<MessageCreateSpec> messagespec;
    /**
     * The input stream to read and attach.
     */
    private InputStream stream;
    /**
     * The name of the attachment that should be shown in Discord.
     */
    private String fileName;

    public MessageBuilder() {}

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
        if(this.content == null) {
            this.content = "";
        }
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

        StringBuilder contentBuilder = new StringBuilder(content);
        for (Styles style : styles) {
            contentBuilder = new StringBuilder(style.markdown + contentBuilder + style.reverseMarkdown);
        }
        content = contentBuilder.toString();

        this.content += content;

        return this;
    }

    /**
     * Sets the channel the message will be sent in.
     *
     * @param channelID The channel the message will be sent in.
     * @return The builder instance.
     */
    public Mono<MessageChannel> withChannel(GatewayDiscordClient client, long channelID) {
        
        
        return client.getChannelById(Snowflake.of(channelID))
                .filter(channel -> channel instanceof MessageChannel)
                .map(channel -> (MessageChannel)channel)
                .doOnSuccess(channel -> this.channel = channel);
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
    public MessageBuilder withEmbed(Consumer<EmbedCreateSpec> embed) {
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
    public Consumer<MessageCreateSpec> build() {
        messagespec = (spec) -> {
            spec.setContent(content);
            if (stream != null)
                spec.addFile(fileName, stream);
            if (embed != null)
                spec.setEmbed(embed);
        };
        return messagespec;
    }

    /**
     * Builds the MessageCreateSpec
     *
     * @param next will be called after the the messagebuilders ones. Useful if you want to edit the message further without the builder
     * @return The built messagespec object.
     */
    public Consumer<MessageCreateSpec> build(Consumer<MessageCreateSpec> next) {
        return spec -> {
            build().accept(spec);
            next.accept(spec);
        };
    }

    /**
     * Sends the message as configured by the builder.
     *
     * @return The sent message object.
     */
    public Mono<Message> send() {
        if(messagespec == null)
            build();
        return channel.createMessage(messagespec);
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
