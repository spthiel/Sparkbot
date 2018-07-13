package me.macro;

public interface IActionFormatter {

	String formatAction(Actions action, FormatObject object, int line, String commandWithArgs, boolean uppercase);

}
