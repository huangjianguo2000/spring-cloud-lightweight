import request from '../utils/request';

export function listServices() {
    return request({
        url: '/light/v1/rc/instance',
        method: 'get',
    })
}