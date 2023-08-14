import request from '../utils/request';

export function listClusterMember() {
    return request({
        url: '/light/v1/rc/cluster',
        method: 'get',
    })
}