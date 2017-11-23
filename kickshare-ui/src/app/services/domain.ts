//@TODO remove this since loading campaig from browser is probably not possible
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
    id: number
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
    id : number;
    name : string;
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

export interface Backer {
    id: number,
    name: string,
    surname: string,
    email: string,
    leader_rating: number
    backer_rating: number
    city: string,
    address: Address
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
                backerId: number,
                postCreated: Date,
                public postModified: Date,
                postEditCount: number,
                public postText: string) {
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
    nw_lat?: number;
    nw_lon?: number;
    se_lat?: number;
    se_lon?: number;

    toParams(): string {
        return stringify(this);
    }
}

export class JSONPSearchOptions extends SearchOptions {
    // callback = 'JSONP_CALLBACK';
}

