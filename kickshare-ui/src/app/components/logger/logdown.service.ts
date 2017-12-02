import * as Logdown from "logdown";
import {Logger, LoggerLevelFactory, TemplateFormatter} from "./logger.component";

export class LogdownFactory extends LoggerLevelFactory {

    newInstance(name: string) {
        return new LogdownLogger(name);
    }
}

export class LogdownLogger extends TemplateFormatter implements Logger {
    private logger;

    constructor(private name: string) {
        super();
        this.logger = new Logdown(name, {markdown: false});
    }

    error(message: string, ...args: any[]) {
        this.logger.error(this.format(message, args));
    }

    warn(message: string, ...args: any[]) {
        this.logger.warn(this.format(message, args));
    }

    info(message: string, ...args: any[]) {
        this.logger.info(this.format(message, args));
    }

    debug(message: string, ...args: any[]) {
        this.logger.debug(this.format(message, args));
    }

    trace(message: string, ...args: any[]) {
        this.logger.trace(this.format(message, args))
    }
}