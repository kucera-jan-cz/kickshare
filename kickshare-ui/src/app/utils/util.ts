
export default function remove(array: Array<any>, element: any) {
    const index = array.indexOf(element);
    array.splice(index, 1);
}