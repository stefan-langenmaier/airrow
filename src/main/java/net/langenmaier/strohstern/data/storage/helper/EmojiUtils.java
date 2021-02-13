package net.langenmaier.strohstern.data.storage.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {
	public static String stripNonEmojis(String text) {
		StringBuilder stripped = new StringBuilder();

		// extracted from the library
		final Pattern htmlEntityPattern = Pattern.compile("&#\\w+;");

		String htmlifiedText = emoji4j.EmojiUtils.htmlify(text);
		// regex to identify html entitities in htmlified text
		Matcher matcher = htmlEntityPattern.matcher(htmlifiedText);

		while (matcher.find()) {
			String emojiCode = matcher.group();
			if (emoji4j.EmojiUtils.isEmoji(emojiCode)) {
				stripped.append(emojiCode);
			}
		}

		return emoji4j.EmojiUtils.emojify(stripped.toString());
	}

}
