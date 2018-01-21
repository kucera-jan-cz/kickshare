export default function remove(array: Array<any>, element: any) {
    const index = array.indexOf(element);
    array.splice(index, 1);
}

export function loadScript(src: string, callback?: (this: HTMLElement, ev: Event) => any) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    if (callback) {
        script.onload = callback;
    }
    document.getElementsByTagName("head")[0].appendChild(script);
    script.src = src;
}

export function random(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}