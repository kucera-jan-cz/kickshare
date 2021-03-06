// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
    production: false,
    landing_page: "/{0}/group/1",
    google_maps_enabled: true,
    loggers: 'services:*,components:*,pages:*',
    root: 'DEBUG',
    error_info: '',
    warn_info: '',
    log_info: 'components:google:geo:factory,components:group,',
    debug_info: 'components:group:members,services:group,component:landing-page',
    trace_info: 'services:country:guard,services:system',
    off_info: ''
};
