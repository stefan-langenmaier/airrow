package net.langenmaier.airrow.backend.app.helper;

public class EmojiUtils {
	private static final int MAX_EMOJI_CODEPOINTS = 4;

	public static String stripNonEmojis(String text) {
		StringBuilder stripped = new StringBuilder();

		int chars[] = text.codePoints().toArray();
		codepoints:
		for (int i=0; i<chars.length; ) {
			for (int l=MAX_EMOJI_CODEPOINTS; l>0; --l) {
				if ((i+l)> chars.length) continue;
				String test = "";
				for (int j=0; j<l; ++j) {
					test += "&#" + chars[i+j] + ";";
				}
				if (emoji4j.EmojiUtils.isEmoji(test)) {
					stripped.append(emoji4j.EmojiUtils.emojify(test));
					i += l;
					continue codepoints;
				}
			}
			i += 1;
		}

		return stripped.toString();
	}

}
