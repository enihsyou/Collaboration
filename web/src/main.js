// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router';
import ElementUI from 'element-ui';
import axios from 'axios';
// fade/zoom 等
import 'element-ui/lib/theme-chalk/base.css';
import RouterConfig from './router'
import App from './App'

const util = {
  env: process.env.NODE_ENV,
  protocol: process.env.NODE_ENV === 'development' ? 'http:' : window.location.protocol,
  baseURL: '112.65.79.238:8999',
  isNumber(input) {
    switch (typeof input) {
      case 'number':
        return true;
      case 'string':
        return /^\d+$/.test(input);
      default:
        return false;
    }
  },
  //验证传入数据是否为空，支持多值
  isEmpty(...args) {
    let res = false;
    for (let value of args) {
      switch (typeof value) {
        case 'number':
          res = value === 0;
          break;
        case 'object':
          res = value instanceof Array ? value.length === 0 : false;
          break;
        case 'string':
          res = value.length === 0 || value.length === 1 && Number.parseInt(value) === 0;
          break;
        case 'undefined':
          res = true;
          break;
        default:
      }
      if (res === true) return res;
    }
    return res;
  },
  //验证是否为email地址
  isMail(adress) {
    return typeof adress === 'string' ? /^[a-zA-Z0-9_-]+@[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)+$/.test(adress) : false;
  },
  //延迟函数
  sleep(time) {
    return new Promise((resolve, reject) => {
      setTimeout(resolve, time);
    })
  },
  fullScreen() {
    const el = document.documentElement;
    let rfs = el.requestFullScreen || el.webkitRequestFullScreen ||
      el.mozRequestFullScreen || el.msRequestFullScreen;
    if (typeof rfs !== 'undefined' && rfs) {
      rfs.call(el);
    } else if (typeof window.ActiveXObject !== 'undefined') {
      //for IE，这里其实就是模拟了按下键盘的F11，使浏览器全屏
      const wscript = new ActiveXObject('WScript.Shell');
      if (wscript != null) {
        wscript.SendKeys('{F11}');
      }
    }
  },
  exitFullScreen() {
    const el = document;
    let cfs = el.cancelFullScreen || el.webkitCancelFullScreen ||
      el.mozCancelFullScreen || el.exitFullScreen;
    if (typeof cfs !== 'undefined' && cfs) {
      cfs.call(el);
    } else if (typeof window.ActiveXObject !== 'undefined') {
      //for IE，这里和fullScreen相同，模拟按下F11键退出全屏
      const wscript = new ActiveXObject('WScript.Shell');
      if (wscript != null) {
        wscript.SendKeys('{F11}');
      }
    }
  }
};

//创建axios
util.ajax = axios.create({
  /*开发环境如果要使用在线接口 请使用8999端口
  * 生产环境请将API请求发到静态文件部署端口会使用nginx进行URL重写*/
  baseURL: `${util.protocol}//${util.baseURL}/api`,
  headers: {
    'content-type': 'application/json;charset=UTF-8'
  },
  timeout: 30 * 1000,
  withCredentials: true,
});

//添加并发管理
util.ajax.all = axios.all;

// 添加请求拦截器
util.ajax.interceptors.request.use(function (config) {
  return config;
}, function (error) {
  return Promise.reject(error);
});

// 添加响应拦截器
util.ajax.interceptors.response.use((response) => {
    if (response.data.code !== '0') {
      return Promise.reject({
        code: response.data.code,
        msg: response.data.msg
      });
    }
    return response.data.data;
  }, (err) => {
    let msg = err.message;
    if (!err.status && msg === 'Network Error') {
      msg = '网络错误'
    }
    return Promise.reject({
      code: err.status,
      msg: msg
    });
  }
);

//时间格式化
Date.prototype.format = function (fmt) {
  const o = {
    'M+': this.getMonth() + 1, //月份
    'D+': this.getDate(), //日
    'h+': this.getHours(), //小时
    'm+': this.getMinutes(), //分
    's+': this.getSeconds(), //秒
    'q+': Math.floor((this.getMonth() + 3) / 3), //季度
    'S': this.getMilliseconds() //毫秒
  };
  if (/(Y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
  for (const k in o)
    if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
  return fmt;
};

const router = new VueRouter(RouterConfig);
router.beforeEach((to, from, next) => {
  //设置标签标题
  let title = to.meta.title ? to.meta.title : '';
  window.document.title = title ? `${title} - NTM协同文档系统` : 'NTM协同文档系统';
  if (to.matched.some(record => record.meta.requiresRole && !record.meta.requiresRole[sessionStorage.role])) {
    console.log('权限错误，当前用户组：' + sessionStorage.role);
    ElementUI.message.error(sessionStorage.role ? '用户组越权警告' : '请先登录');
    next({
      path: '/login',
      query: {//把要跳转的地址作为参数传到下一步
        redirect: to.path
      }
    });
    return;
  }
  //需要认证token的网页
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (util.isEmpty(sessionStorage.token)) {
      //本地token不存在
      if (process.env.NODE_ENV === 'development') {
        //开发模式跳过登录验证
        console.log('开发者模式-免验证跳转（本地token不存在）');
        next();
        return;
      }
      ElementUI.message.error('请先登录');
      next({
        path: '/login',
        query: {//把要跳转的地址作为参数传到下一步
          redirect: to.path
        }
      });
    } else {
      //存在则发送到服务器验证
      util.ajax.get('/token').then((res) => {
        next();
      }, (errRes) => {
        if (process.env.NODE_ENV === 'development') {
          //开发模式跳过登录验证
          console.log('开发者模式-免验证跳转（本地token已失效）');
          next();
          return;
        }
        ElementUI.message.error('登录已失效，请重新登录');
        next({
          path: '/login',
          query: {//把要跳转的地址作为参数传到下一步
            redirect: to.path
          }
        });
      });
    }
  } else {
    //无需验证
    next();
  }
});

router.afterEach(() => {
  window.scrollTo(0, 0);
});

Vue.prototype.$ = util;
Vue.use(ElementUI);
Vue.use(VueRouter);

new Vue({
  el: '#app',
  router: router,
  render: h => h(App)
});
