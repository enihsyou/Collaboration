import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
    //   name: 'index',
    //   component: (resolve) => require(['../components/index'], resolve)
    // },
    // {
    //   path: '/articlePannel',
    //   name: 'articlePanel',
      component: (resolve) => require(['../components/articlePanel'], resolve)
    },
  ]
})
