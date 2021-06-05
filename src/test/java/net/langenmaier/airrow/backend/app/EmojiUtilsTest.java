package net.langenmaier.airrow.backend.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.langenmaier.airrow.backend.app.helper.EmojiUtils;

class EmojiUtilsTest {

	@Test
	void testStripNonEmojis() {
		assertEquals("", EmojiUtils.stripNonEmojis("nothing"));
		assertEquals("🍺🌽", EmojiUtils.stripNonEmojis("🍺🌽"));
		assertEquals("🍺🌽", EmojiUtils.stripNonEmojis("🍺text🌽"));
		assertEquals("📖", EmojiUtils.stripNonEmojis("📖"));
		
		// this is not a recommended emoji
		// https://emojipedia.org/book/
		// assertEquals("🕮", EmojiUtils.stripNonEmojis("🕮"));
		
		assertEquals("🏳️‍🌈", EmojiUtils.stripNonEmojis("🏳️‍🌈"));
		assertEquals("🎞️", EmojiUtils.stripNonEmojis("\ud83c\udf9e\ufe0f"));
		assertEquals("🎞️", EmojiUtils.stripNonEmojis("\ud83c\udf9e"));
		assertEquals("\ud83c\udf9e\ufe0f", EmojiUtils.stripNonEmojis("🎞️"));
		// assertEquals("\ud83c\udf9e", EmojiUtils.stripNonEmojis("🎞️"));

		
	}

}
