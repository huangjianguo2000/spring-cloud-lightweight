<template>
  <div>
    <div class="container">
<!--      <div class="handle-box">-->

<!--        <el-input v-model="query.name" placeholder="" class="handle-input mr10"></el-input>-->
<!--        <el-button type="primary" icon="el-icon-search" @click="getData">查询</el-button>-->
<!--      </div>-->
      <el-table v-loading="loading" :data="clusterList" border class="table" ref="multipleTable"
                header-cell-class-name="table-header">
        <el-table-column prop="ip" label="ip" width="300" align="center"></el-table-column>
        <el-table-column prop="port" label="port" width="200" align="center"></el-table-column>
        <el-table-column prop="state" label="state" width="200" align="center"></el-table-column>
<!--        <el-table-column label="操作" align="center">-->
<!--          <template #default="scope">-->
<!--            <el-button type="text" @click="lookDetail( scope.row)">查看-->
<!--            </el-button>-->
<!--            <el-button type="text"-->
<!--                       @click="deleteService(scope.row)">删除-->
<!--            </el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->

      </el-table>

    </div>
  </div>
</template>

<script>
import {listClusterMember} from "../api/cluster";

export default {
  name: "Cluster",
  data() {
    return {
      clusterList: [],
      query: {},
      loading: false
    }
  },
  created() {
    this.getData();
  },
  methods:{
    getData() {
      this.loading = true;
      listClusterMember().then(res => {
        this.loading = false;
        this.clusterList = res.data.data;
        console.log(this.clusterList);
      })
    },
  }
}
</script>

<style scoped>

</style>