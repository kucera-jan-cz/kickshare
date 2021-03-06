import {environment} from "../../../environments/environment";
import {sprintf} from 'sprintf-js';
import * as format from "string-template";

/**
 * Logging wrapper class for easier moving to any logging framework
 * Created by KuceraJan on 18.6.2017.
 */

export interface Logger {

    error(message: string, ...args: Array<any>);

    warn(message: string, ...args: Array<any>);

    info(message: string, ...args: Array<any>);

    debug(message: string, ...args: any[]);

    trace(message: string, ...args: any[]);
}

export interface Formatter {
    format(message: string, ...args: any[]);
}

export class NoLogger implements Logger {
    error(message: string, ...args: any[]) {
    }

    warn(message: string, ...args: any[]) {
    }

    info(message: string, ...args: any[]) {
    }

    debug(message: string, ...args: any[]) {
    }

    trace(message: string, ...args: any[]) {
    }
}

export class TemplateFormatter implements Formatter {

    format(message: string, ...args: Array<any>): string {
        return format(message, args)
    }
}

export class SprintfFormatter implements Formatter {

    format(message: string, ...args: any[]): string {
        return sprintf(message, args)
    }
}

export interface ILoggerFactory {
    getLogger(name: string): Logger;
}

export abstract class LoggerLevelFactory implements ILoggerFactory {
    static offLogger = new NoLogger();
    private debugEnabled = false;
    private rootLevel: Level = Level[environment.root];
    private errorRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.error_info);
    private warnRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.warn_info);
    private infoRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.log_info);
    private debugRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.debug_info);
    private traceRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.trace_info);
    private offRegexes: RegExp[] = LoggerLevelFactory.parseInfo(environment.off_info);

    abstract newInstance(name: string): Logger;

    static parseInfo(info: string) {
        return info
            .split(",")
            .filter(i => i)
            .map(i => i + '$')
            .map(r => new RegExp(r));
    }

    public getLogger(name: string): Logger {
        this.debug("Choosing log level for " + name);
        var level: Level = this.getLevel(name);
        return new LogLevel(level, this.newInstance(name));
    }


    getLevel(name: string): Level {
        const packages= this.getPackages(name);
        var level;
        for(var i = 0; i < packages.length; i++) {
            level = this.matchLevel(packages[i]);
            if(level != null) {
                return level;
            }
        }
        level = this.rootLevel;
        this.debug("DEFAULT level chosen - " + Level[this.rootLevel]);
        return level;
    }

    private getPackages(name: string) : string[] {
        let parts = name.split(":");
        if(parts.length < 2) {
            return Array.of(name);
        }
        const packages = Array.of(parts[0]);
        for(var i = 1; i < parts.length; i++) {
            let previous = packages[i-1];
            packages.push(`${previous}:${parts[i]}`);
        }
        return packages.reverse();
    }

    private matchLevel(name: string): Level {
        this.debug("Matching level: "+name);
        if (this.offRegexes.some(r => r.test(name))) {
            this.debug("OFF level chosen for " + name);
            return Level.OFF;
        }
        if (this.traceRegexes.some(r => r.test(name))) {
            this.debug("TRACE level chosen for " + name);
            return Level.TRACE;
        }
        if (this.debugRegexes.some(r => r.test(name))) {
            this.debug("DEBUG level chosen for " + name);
            return Level.DEBUG;
        }
        if (this.infoRegexes.some(r => r.test(name))) {
            this.debug("INFO level chosen for " + name);
            return Level.INFO;
        }
        if (this.warnRegexes.some(r => r.test(name))) {
            this.debug("WARN level chosen for " + name);
            return Level.WARN;
        }
        if (this.errorRegexes.some(r => r.test(name))) {
            this.debug("ERROR level chosen for " + name);
            return Level.ERROR;
        }

    }

    debug(msg: string) {
        this.debugEnabled ? console.info(msg) : null;
    }
}

export enum Level {
    OFF = 10,
    ERROR = 4,
    WARN = 3,
    INFO = 2,
    DEBUG = 1,
    TRACE = 0
}


export class LogLevel implements Logger {
    constructor(private level: Level, private logger: Logger) {
    }

    error(message: string, ...args: Array<any>) {
        if (this.level > Level.ERROR) {
        } else {
            this.logger.error(message, args);
        }
    }

    warn(message: string, ...args: Array<any>) {
        if (this.level > Level.WARN) {
        } else {
            this.logger.warn(message, args);
        }
    }

    info(message: string, ...args: Array<any>) {
        if (this.level > Level.INFO) {
        } else {
            this.logger.info(message, args);
        }
    }

    debug(message: string, ...args: any[]) {
        if (this.level > Level.DEBUG) {
        } else {
            this.logger.debug(message, args);
        }
    }

    trace(message: string, ...args: any[]) {
        if (this.level == Level.TRACE) {
            this.logger.trace(message, args);
        }
    }
}