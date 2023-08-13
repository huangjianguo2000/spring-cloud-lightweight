<template>
  <div>
    <div class="container">
      <div>
        <h1>服务详情</h1>
        <el-form class="from" ref="form" :model="service" label-width="80px">
          <el-form-item label="服务名称:  ">
            <p>{{service.serviceName}}</p>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="service.hosts" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="ip" label="IP" width="300" align="center"></el-table-column>
        <el-table-column prop="port" label="端口" width="200" align="center"></el-table-column>
<!--        <el-table-column prop="weight" label="权重" width="200" align="center"></el-table-column>-->
        <el-table-column prop="healthy" label="是否健康" width="200" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="text" @click="lookDetail( scope.row)">编辑
            </el-button>
            <el-button type="text"
                       @click="offlineService(scope.row)">下线
            </el-button>
          </template>
        </el-table-column>

      </el-table>

    </div>


  </div>
</template>

<script>
import {listServices} from "../api/service";

export default {
  name: "ServiceDetail",
  data(){
    return{
      service:{},
      query:{

      }
    }
  },
  created() {
    let servicesName = this.$route.params.serviceName;
    this.getData(servicesName);
  },
  methods:{
    getData(servicesName) {
      listServices().then(res => {
        for(let i = 0; i < res.data.data.length; i++){
          console.log(res.data.data[i].serviceName + "--" + servicesName)
          if(res.data.data[i].serviceName === servicesName){
            this.service = res.data.data[i];
          }
        }
      })
    },
    // 查询服务详情
    lookDetail(row){
      this.$message.error("还没有这个功能");
    },
    // 下线服务
    offlineService(row){
      this.$message.error("还没有这个功能");
    }
  }
}
</script>

<style scoped>
h1{
  margin-bottom: 10px;
  font-weight: 400;
}
.from{
  width: 400px;
}
</style>