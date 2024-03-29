import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";

const routes = [
    {
        path: '/',
        redirect: '/service'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/service",
                name: "service",
                meta: {
                    title: '服务列表'
                },
                component: () => import ( /* webpackChunkName: "dashboard" */ "../views/Service.vue")
            }, {
                path: "/serviceDetail/:serviceName",
                name: "serviceDetail",
                meta: {
                    title: '服务详情'
                },
                component: () => import ( /* webpackChunkName: "dashboard" */ "../views/ServiceDetail.vue")
            },  {
                path: "/cluster",
                name: "cluster",
                meta: {
                    title: '集群列表'
                },
                component: () => import ( /* webpackChunkName: "dashboard" */ "../views/Cluster.vue")
            }, {
                path: '/404',
                name: '404',
                meta: {
                    title: '找不到页面'
                },
                component: () => import (/* webpackChunkName: "404" */ '../views/404.vue')
            }, {
                path: '/403',
                name: '403',
                meta: {
                    title: '没有权限'
                },
                component: () => import (/* webpackChunkName: "403" */ '../views/403.vue')
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import ( /* webpackChunkName: "login" */ "../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | vue-manage-system`;
    const role = localStorage.getItem('ms_username');
    if (!role && to.path !== '/login') {
        next('/login');
    } else if (to.meta.permission) {
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin'
            ? next()
            : next('/403');
    } else {
        next();
    }
});

export default router;