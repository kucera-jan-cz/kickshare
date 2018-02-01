import {stringify} from "query-string";

export interface Campaign {
    id: number
    name: string
    description: string
    url: string
    deadline: number
    photo_url: string
    photo: CampaignPhoto
    //deadline
    //link to KS
    //TBD tags?
}

export interface CampaignPhoto {
    thumb: string
    small: string
    little: string
    ed: string
    med: string
    full: string
}

export interface Category {
    name: string;
    id: number;
    order?: number
}

export interface Group {
    name: string
    id?: number
    project_id?: number
    group_id?: number
    leader_id?: number
    leader_name?: string
    leader_rating?: number
    is_local?: boolean
    participant_count?: number
    limit?: number
}

export interface City {
    id: number;
    name: string;
}

export interface GroupInfo {
    project: Project,
    photo: CampaignPhoto,
    group: Group,
    leader: Backer,
    backers: Backer[],
}

export interface GroupSummary {
    project: Project,
    photo: CampaignPhoto,
    group: Group,
    leader: Backer
}

export class Backer {
    id: number;
    name: string;
    surname: string;
    email: string;
    leader_rating: number;
    backer_rating: number;
    city: string;
    address: Address;

    get fullname() {
        return `${this.name} ${this.surname}`;
    }
}

export interface Address {
    backer_id: number
    street: string,
    city: string,
    postal_code: string
}

export class Post {
    constructor(postId: number,
                groupId: number,
                public backerId: number,
                postCreated: Date,
                public postModified: Date,
                postEditCount: number,
                public postText: string) {
    }
}

export class MessagePost {
    public postId: number;
    public author: string;
    public backerId: number;
    public postCreated: Date;
    public postModified: Date;
    public postText: string;
}

export class PageRequest {
    constructor(public page: number, public size: number = 10, public seek: string = "1") {
    }

    toParams(): string {
        return `size=${this.size}&page=${this.page}&seek=${this.seek}`
    }
}

export class Page {
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    numberOfElements: number;
    first: boolean;
}

export class Pageable<T> extends Page {
    content: Array<T>;
    seek: string;


}

export class Tag {
    constructor(public id: number, public name: string) {
    }
}

export interface Project {
    id: number,
    name: string,
    url: string,
    description?: string,
    deadline: Date
    //deadline
    //TBD tags?
}

export interface ProjectInfo extends Project {
    photo_url: string,
    photo: CampaignPhoto
}

export interface Notification {
    id: number,
    backer_id: number,
    sender_id: number,
    postCreated: Date,
    isRead: boolean
    postText: string
}


export class SearchOptions {
    //@TODO - remove typescript null options
    // callback = 'JSONP_CALLBACK';
    only_local?: boolean;
    name?: string;
    project_id?: number;
    category_id: number;
    nw_lat?: number;
    nw_lon?: number;
    se_lat?: number;
    se_lon?: number;

    toParams(): string {
        return stringify(this);
    }
}

export class Country {
    constructor(public name: string,
                public code: string) {
    }
}

export class JSONPSearchOptions extends SearchOptions {
    // callback = 'JSONP_CALLBACK';
}

