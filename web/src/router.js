export default {
  mode: 'history',
  routes: [
    {
      path: '/',
      component: (resolve) => require(['./components/entry/layout'], resolve),
      children: [
        {
          path: 'login',
          meta: {
            title: '欢迎登录',
          },
          component: (resolve) => require(['./components/entry/login'], resolve)
        },
        {
          path: 'register',
          meta: {
            title: '注册新用户',
          },
          component: (resolve) => require(['./components/entry/register'], resolve)
        },
        {
          path: 'forgot',
          meta: {
            title: '忘记密码',
          },
          component: (resolve) => require(['./components/entry/forgot'], resolve)
        },
        {
          path: 'retrieve',
          meta: {
            title: '找回密码',
          },
          component: (resolve) => require(['./components/entry/retrieve'], resolve)
        },
        {
          path: '',
          redirect: '/login'
        }
      ]
    },
    {
      path: '/user',
      component: (resolve) => require(['./components/article/layout'], resolve),
      children: [
        {
          path: ':id',
          meta: {
            title: '编辑文章',
          },
          component: (resolve) => require(['./components/article/articlePanel'], resolve)
        }
      ]
    }
  ]
}
