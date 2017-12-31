// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
    production: false,
    loggers: 'services:*,components:*,pages:*',
    root: 'WARN',
    error_info: '',
    warn_info: '',
    log_info:   'components:google:geo:factory,components:group',
    debug_info: 'components:group:members,services:group',
    trace_info: '',
    off_info: ''

};
