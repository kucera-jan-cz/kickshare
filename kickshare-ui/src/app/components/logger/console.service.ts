import {Logger, LoggerLevelFactory, TemplateFormatter} from "./logger.component";

/**
 * Logging wrapper class for easier moving to any logging framework
 * Created by KuceraJan on 18.6.2017.
 */
export class LogConsoleService extends TemplateFormatter implements Logger {
    error(message: string, ...args: any[]) {
        console.error(this.format(message, args));
    }

    warn(message: string, ...args: any[]) {
        console.warn(this.format(message, args));
    }

    info(message: string, ...args: any[]) {
        console.info(this.format(message, args));
    }

    debug(message: string, ...args: any[]) {
        console.debug(this.format(message, args))
    }

    trace(message: string, ...args: any[]) {
        console.trace(this.format(message, args))
    }
}

export class ConsoleLoggerFactory extends LoggerLevelFactory {
    newInstance(name: string): Logger {
        return new LogConsoleService();
    }

}
