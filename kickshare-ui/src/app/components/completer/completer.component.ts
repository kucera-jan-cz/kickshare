import {CompleterBaseData, CompleterItem} from "ng2-completer";

export class Completer extends CompleterBaseData {
    constructor(private searchFunction: (text: string) => Promise<any[]>, private title: string) {
        super();
        this.titleField(title);
    }

    search(term: string): void {
        this.searchFunction(term).then(
            result => {
                console.info("Result: " + JSON.stringify(result));
                const items: CompleterItem[] = result.map(data => this.convertToItem(data));
                console.info("Passing: " + JSON.stringify(items));
                this.next(items);
            }
        )
    }

}