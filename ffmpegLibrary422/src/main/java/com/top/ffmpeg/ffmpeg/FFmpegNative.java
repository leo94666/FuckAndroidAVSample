package com.top.ffmpeg.ffmpeg;


import java.util.ArrayList;
import java.util.List;

/**
 * @author leo
 * @version 1.0
 * @className FFmpegConfig
 * @description TODO
 * @date 2022/4/8 16:08
 **/
public class FFmpegNative {

    static {
        System.loadLibrary("SuperNativeRender");
    }

    public static String ffmpegVersion() {
       return getNativeFFmpegVersion();
    }


    public static void ffmpegExecute(final FFmpegSession ffmpegSession) {
        ffmpegSession.startRunning();
        try {
            final int returnCode = nativeFFmpegExecute(ffmpegSession.getSessionId(), ffmpegSession.getArguments());
            ffmpegSession.complete(new ReturnCode(returnCode));
        } catch (final Exception e) {
            ffmpegSession.fail(e);
        }
    }





    public static String[] parseArguments(final String command) {
        final List<String> argumentList = new ArrayList<>();
        StringBuilder currentArgument = new StringBuilder();

        boolean singleQuoteStarted = false;
        boolean doubleQuoteStarted = false;

        for (int i = 0; i < command.length(); i++) {
            final Character previousChar;
            if (i > 0) {
                previousChar = command.charAt(i - 1);
            } else {
                previousChar = null;
            }
            final char currentChar = command.charAt(i);

            if (currentChar == ' ') {
                if (singleQuoteStarted || doubleQuoteStarted) {
                    currentArgument.append(currentChar);
                } else if (currentArgument.length() > 0) {
                    argumentList.add(currentArgument.toString());
                    currentArgument = new StringBuilder();
                }
            } else if (currentChar == '\'' && (previousChar == null || previousChar != '\\')) {
                if (singleQuoteStarted) {
                    singleQuoteStarted = false;
                } else if (doubleQuoteStarted) {
                    currentArgument.append(currentChar);
                } else {
                    singleQuoteStarted = true;
                }
            } else if (currentChar == '\"' && (previousChar == null || previousChar != '\\')) {
                if (doubleQuoteStarted) {
                    doubleQuoteStarted = false;
                } else if (singleQuoteStarted) {
                    currentArgument.append(currentChar);
                } else {
                    doubleQuoteStarted = true;
                }
            } else {
                currentArgument.append(currentChar);
            }
        }

        if (currentArgument.length() > 0) {
            argumentList.add(currentArgument.toString());
        }

        return argumentList.toArray(new String[0]);
    }

    public static String argumentsToString(final String[] arguments) {
        if (arguments == null) {
            return "null";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(arguments[i]);
        }

        return stringBuilder.toString();
    }

    ///////////////////////////////////////////////////////////
    /**
     * <p>Enables redirection natively.
     */
     public static native void enableNativeRedirection();

    /**
     * <p>Disables redirection natively.
     */
     public static native void disableNativeRedirection();

    /**
     * Returns native log level.
     *
     * @return log level
     */
    public static native int getNativeLogLevel();

    /**
     * Sets native log level
     *
     * @param level log level
     */
    public static native void setNativeLogLevel(int level);

    /**
     * <p>Returns FFmpeg version bundled within the library natively.
     *
     * @return FFmpeg version
     */
    public static native String getNativeFFmpegVersion();

    /**
     * <p>Returns FFmpegKit library version natively.
     *
     * @return FFmpegKit version
     */
    public static native String getNativeVersion();

    /**
     * <p>Synchronously executes FFmpeg natively.
     *
     * @param sessionId id of the session
     * @param arguments FFmpeg command options/arguments as string array
     * @return {@link ReturnCode#SUCCESS} on successful execution and {@link ReturnCode#CANCEL} on
     * user cancel. Other non-zero values are returned on error. Use {@link ReturnCode} class to
     * handle the value
     */
    public static native int nativeFFmpegExecute(final long sessionId, final String[] arguments);

    /**
     * <p>Synchronously executes FFprobe natively.
     *
     * @param sessionId id of the session
     * @param arguments FFprobe command options/arguments as string array
     * @return {@link ReturnCode#SUCCESS} on successful execution and {@link ReturnCode#CANCEL} on
     * user cancel. Other non-zero values are returned on error. Use {@link ReturnCode} class to
     * handle the value
     */
    public static native int nativeFFprobeExecute(final long sessionId, final String[] arguments);

    /**
     * <p>Cancels an ongoing FFmpeg operation natively. This method does not wait for termination
     * to complete and returns immediately.
     *
     * @param sessionId id of the session
     */
    public static native void nativeFFmpegCancel(final long sessionId);

    /**
     * <p>Returns the number of native messages that are not transmitted to the Java callbacks for
     * this session natively.
     *
     * @param sessionId id of the session
     * @return number of native messages that are not transmitted to the Java callbacks for
     * this session natively
     */
    public static native int messagesInTransmit(final long sessionId);

    /**
     * <p>Creates a new named pipe to use in <code>FFmpeg</code> operations natively.
     *
     * <p>Please note that creator is responsible of closing created pipes.
     *
     * @param ffmpegPipePath full path of ffmpeg pipe
     * @return zero on successful creation, non-zero on error
     */
    public static native int registerNewNativeFFmpegPipe(final String ffmpegPipePath);

    /**
     * <p>Returns FFmpegKit library build date natively.
     *
     * @return FFmpegKit library build date
     */
    public static native String getNativeBuildDate();

    /**
     * <p>Sets an environment variable natively.
     *
     * @param variableName  environment variable name
     * @param variableValue environment variable value
     * @return zero on success, non-zero on error
     */
    public static native int setNativeEnvironmentVariable(final String variableName, final String variableValue);

    /**
     * <p>Registers a new ignored signal natively. Ignored signals are not handled by
     * <code>FFmpegKit</code> library.
     *
     * @param signum signal number
     */
    public static native void ignoreNativeSignal(final int signum);


}
