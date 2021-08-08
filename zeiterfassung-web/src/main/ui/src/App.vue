<template>
  <div id="app">
    <img width="25%" src="./assets/logo.png">
    <ZeiterfassungOverview
      class="zeiterfassungOverview"
      @refreshUi="refreshRequested"
      :key="overviewKey"
    />
    <SetActualHoursComparison
      class="setActualHoursComparison"
      :key="setActualHoursComparisonKey"
    />
    <ZeiterfassungDashboard class="zeiterfassungDashboard"/>
    <ZeiterfassungRecordingStatus
      class="zeiterfassungRecordingStatus"
      :key="zeiterfassungStatusKey"
      @refreshUi="refreshRequested"
    />
  </div>
</template>

<script>
import ZeiterfassungDashboard from './views/ZeiterfassungDashboard.vue';
import ZeiterfassungRecordingStatus from './views/ZeiterfassungRecordingStatus.vue';
import ZeiterfassungOverview from './views/ZeiterfassungOverview.vue';
import SetActualHoursComparison from './views/SetActualHoursComparison.vue';

export default {
  name: 'App',
  components: {
    ZeiterfassungDashboard,
    ZeiterfassungRecordingStatus,
    ZeiterfassungOverview,
    SetActualHoursComparison,
  },
  data () {
   return {
     overviewKey: 0,
     zeiterfassungStatusKey: 0,
     setActualHoursComparisonKey: 0,
   };
 },
  methods: {
    refreshRequested: function(){
      this.overviewKey += 1;
      this.setActualHoursComparisonKey += 1;
      this.zeiterfassungStatusKey += 1;
      console.log("App.vue: Refresh requested! overviewKey ''" + this.overviewKey + "'', setActualHoursComparisonKey: '" + this.setActualHoursComparisonKey + "''");
    }
  },
}
</script>

<style>
  * {
   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }

  button{
    border-collapse: collapse;
    background-color:#004587;
    color:white;
  }

  button:disabled{
    border-collapse: collapse;
    background-color:lightslategray;
  }

  table, th, td {
    color:white;
    overflow: hidden;
    table-layout:dynamic;
    border-collapse: collapse;
    text-align: left;
    padding: 5px;
  }

  table {
    background-color:#358fe6;
  }

  th {
    background-color:#004587;
  }

  button{
    table-layout: auto;
  }

  .container{
    display: flex;
    justify-content: space-between;
    max-width: 400px;
    margin: 5 auto;
    padding: 0 0 0 0;
  }

  .container0{
    display: inline-block;
    justify-content: space-between;
  }

  .containerElement{
    width: 49.5%;
  }

  .containerElement100{
    width: 100%;
  }

  .center {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .zeiterfassungDashboard{
    position: fixed;
    top: 20%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .setActualHoursComparison{
    position: fixed;
    top: 33%;
    left: 82%;
  }

  .zeiterfassungRecordingStatus{
    position: fixed;
    width: 20%;
    top: 33%;
    left: 20%;
  }

  .zeiterfassungOverview{
    position: fixed;
    width: 50%;
    top: 50%;
    left: 20%;
  }

</style>
