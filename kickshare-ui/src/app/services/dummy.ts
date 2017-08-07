import {Group, Project} from "./domain";
/**
 * Created by KuceraJan on 9.4.2017.
 */
export const PROJECTS: Project[] = [
    {
        id: 439380282,
        name: 'Quodd Heroes',
        url: 'https://ksr-ugc.imgix.net/assets/015/136/434/e087701618d6667ec7f5aae3044d9353_original.jpg?w=160&h=90&fit=fill&bg=000000&v=1488474152&auto=format&q=92&s=9076cf32a24159a78cd9028af6bbb30f',
        deadline: null
    },
    {
        id: 217227567,
        name: 'The World of SMOG: Rise of Moloch',
        url: 'https://ksr-ugc.imgix.net/assets/015/136/052/c12d054c0e5007dc7bbb13e0f7b91c24_original.png?w=160&h=90&fit=fill&bg=000000&v=1484162980&auto=format&q=92&s=cfa4c11446ed3379064c92c2e82fc3a0',
        deadline: null
    },
    {
        id: 1893061183,
        name: 'The Edge: Dawnfall',
        url: 'https://ksr-ugc.imgix.net/assets/013/983/274/cbb6e3b30a9482ae124213c1014f62b7_original.jpg?w=160&h=90&fit=fill&bg=000000&v=1478596655&auto=format&q=92&s=12b1cce4f84672f34c742b99ebbeaab3',
        deadline: null
    },
    {
        id: 1239127362,
        name: 'Perfect Crime: A cooperative heist game for 2-5 players',
        url: 'https://ksr-ugc.imgix.net/assets/012/533/091/8c5e40f026fa293a207a89ec090fa41b_original.jpg?w=160&h=90&fit=fill&bg=FFFFFF&v=1464275526&auto=format&q=92&s=0525a1702124391505ca25f612f3bfb9',
        deadline: null
    },
];

export const GROUPS: Group[] = [
    {name: 'Quodd Heroes CZ 001', project_id: 439380282, group_id: 1, leader_name: 'Jan Kučera', leader_rating: 4, is_local: false, participant_count: 10},
    {name: 'Rise of Moloch CZ 001', project_id: 217227567, group_id: 2, leader_name: 'Jan Kučera', leader_rating: 4, is_local: false, participant_count: 0},
    {
        name: 'Rise of Moloch CZ Praha',
        project_id: 217227567,
        group_id: 3,
        leader_name: 'Mikolas Orlicky',
        leader_rating: 5,
        is_local: true,
        participant_count: 1
    },
];
