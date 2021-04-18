package net.langenmaier.airrow.backend.app.helper;

import java.io.InputStream;
import java.util.Scanner;

public class FileUtil {

	public static String readString(InputStream resourceAsStream) {
        StringBuilder res = new StringBuilder();
        Scanner in = new Scanner(resourceAsStream);
        while (in.hasNextLine()) {
            res.append(in.nextLine() + "\n");
        }
        in.close();
        return res.toString();
	}

}
