import {Backer, GroupInfo} from "../../../services/domain";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import remove from "../../../utils/util";
import {GroupService} from "../../../services/group.service";

export class GroupMetadata {
    private backersSub: BehaviorSubject<Backer[]>;
    private requestsSub: BehaviorSubject<Backer[]>;

    constructor(private group: GroupInfo, private backerId: number, private groupService: GroupService) {
        this.backersSub = new BehaviorSubject<Backer[]>(group.backers);
        this.requestsSub = new BehaviorSubject<Backer[]>([]);
        if (group.leader.id == backerId) {
            this.groupService.getBackerRequests(this.getId())
                .then(requests => this.requestsSub.next(requests))
        }
    }

    public getId(): number {
        return this.group.group.id;
    }

    public backers(): Observable<Backer[]> {
        return this.backersSub;
    }

    public requests(): Observable<Backer[]> {
        return this.requestsSub;
    }

    public async accept(backer: Backer) {
        const promise = await this.groupService.acceptBacker(this.getId(), backer.id);
        const currentRequests = this.requestsSub.getValue();
        remove(currentRequests, backer);
        const currentBackers = this.backersSub.getValue();
        currentBackers.push(backer);
        this.requestsSub.next(currentRequests);
        this.backersSub.next(currentBackers);
    }

    public async reject(backer: Backer) {
        const promise = await this.groupService.rejectBacker(this.getId(), backer.id);
        const currentRequests = this.requestsSub.getValue();
        remove(currentRequests, backer);
        this.requestsSub.next(currentRequests);
    }

    public async join() {
        const promise = await this.groupService.joinGroup(this.getId(), this.backerId);
        const currentRequests = await this.groupService.getBackerRequests(this.getId());
        this.requestsSub.next(currentRequests);
    }

    public async leave(backer: Backer) {
        const promise = await this.groupService.leaveGroup(this.getId(), backer.id);
        const currentBackers = this.backersSub.getValue();
        remove(currentBackers, backer);
        this.backersSub.next(currentBackers);
    }

    public getBackers(): Backer[] {
        return this.group.backers;
    }

    public getLeader(): Backer {
        return this.group.leader;
    }

    public isMember() {
        return this.backersSub.getValue().find(b => b.id == this.backerId) != null;
    }

    public canJoin(): boolean {
        const alreadyRequested = this.requestsSub.getValue().find(r => r.id == this.backerId) != null;
        return !alreadyRequested && !this.isMember() && !this.isLeader();
    }

    public isBacker() {
        return !this.isLeader() && this.isMember();
    }

    public isLeader() {
        return this.group.leader.id == this.backerId;
    }

    public isRequester() {
        return !this.isMember();
    }

    public isAnonymous() {
        return this.backerId < 1;
    }
}