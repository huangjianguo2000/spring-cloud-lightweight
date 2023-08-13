<template>
  <div>

    <div class="container">
      <div class="handle-box">

        <el-input v-model="query.name" placeholder="" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="getData">查询</el-button>
      </div>
      <el-table v-loading="loading" :data="serviceList" border class="table" ref="multipleTable"
                header-cell-class-name="table-header">
        <el-table-column prop="serviceName" label="服务名称" width="300" align="center"></el-table-column>
        <el-table-column prop="hosts.length" label="实例数" width="200" align="center"></el-table-column>
        <el-table-column prop="healthyNum" label="健康实例数" width="200" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="text" @click="lookDetail( scope.row)">查看
            </el-button>
            <el-button type="text"
                       @click="deleteService(scope.row)">删除
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
  name: "Service",
  data() {
    return {
      serviceList: [],
      query: {},
      loading: false
    }
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      this.loading = true;
      listServices().then(res => {
        this.loading = false;
        this.serviceList = res.data.data;
        // 计算健康实例数量
        this.countHealthyNum();
      })
    },
    // 计算健康实例数量
    countHealthyNum() {
      for (let i = 0; i < this.serviceList.length; i++) {
        let cnt = 0;
        for (let j = 0; j < this.serviceList[i].hosts.length; j++) {
          if (this.serviceList[i].hosts[j].healthy === true) {
            cnt++;
          }
        }
        this.serviceList[i].healthyNum = cnt;
      }
    },
    // 查询服务详情
    lookDetail(row) {
      // console.log(row.healthyNum);
      this.$router.push({name: 'serviceDetail', params: {serviceName: row.serviceName}});
    },
    // 删除服务
    deleteService(row) {
      this.$message.error("还没有这个功能");
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.table {
  width: 100%;
  font-size: 14px;
}

.red {
  color: #ff0000;
}

.mr10 {
  margin-right: 10px;
}

.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
