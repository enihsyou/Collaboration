<template>
  <div>
    <h1>测试stomp</h1>
    <p>
      连接url:
      <el-input v-model="connURL"></el-input>
      <el-button @click="connWS">连接</el-button>
    </p>
    <p>
      订阅url:
      <el-input v-model="subURL"></el-input>
      <el-button @click="subWS">订阅</el-button>
    </p>
    <p>
      发送url:
      <el-input v-model="sentURL"></el-input>
      发送内容：
      <el-input v-model="sentText" type="textarea" style="resize: none">

      </el-input>
      <el-button @click="senWS">发消息</el-button>

    </p>
  </div>
</template>

<script>
  export default {
    name: "stomp",
    data() {
      return {
        connURL: 'ws://localhost:8999/api/websocket',
        subURL: '/topic/pad.9',
        sentURL: '/topic/pad.9',
        sentText: '{"pad_id":"7","revision_id":0,"range":{"start":524,"end":583}}',
        webSocketClient: null
      }
    },
    methods: {
      connWS() {
        const webSocketClient = Stomp.client(this.connURL);
        //设置脉搏时间30s
        webSocketClient.heartbeat.outgoing = 30 * 1000;
        webSocketClient.heartbeat.incoming = 0;
        //设置连接回调
        const connCallback = (frame) => {
          // console.log(`${this.$.protocol.includes('s') ? 'wss' : 'ws'}：与服务器建立连接：${frame}`);
        };
        //设置消息回调
        const msgCallback = (response) => {
          // console.log('收到来自服务器的消息:', response);
        };
        //设置连接头
        const wsHead = {
          // login: 'mylogin',
          // passcode: 'mypasscode',
          // 'client-id': 'my-client-id'
        };
        webSocketClient.connect(wsHead, connCallback);

        webSocketClient.sub = () => {
          //订阅
          webSocketClient.subscribe(this.subURL, msgCallback);
        };

        webSocketClient.sen = () => {
          webSocketClient.send(this.sentURL, {priority: 9}, this.sentText);
        };

        this.webSocketClient = webSocketClient;
      },
      subWS() {
        this.webSocketClient.sub();
      },
      senWS() {
        this.webSocketClient.sen();
      },
    },
    mounted() {
      this.$.ajax.post('/account/login', JSON.stringify({
        username: '123',
        password: '123'
      }));
    }
  }
</script>

<style scoped>

</style>
