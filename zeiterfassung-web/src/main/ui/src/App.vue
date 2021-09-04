<template>
  <div id="app" class="centered">
    <ZeiterfassungDashboard/>
    <div class="content">
      <div class="statusAndBDayOverviewPanelAndActualHoursPanel">
        <div class="timeRecordingStatusAndActualHoursComparison">
          <SetActualHoursComparison
            class="setActualHoursComparison tile"
            :key="setActualHoursComparisonKey"
            @refreshUi="refreshUis"
          />
          <ZeiterfassungRecordingStatus
            class="zeiterfassungRecordingStatus tile"
            :key="zeiterfassungStatusKey"
            @refreshUiWithHistory="refreshAllUisInclHistory"
            @refreshUi="refreshUis"
          />
        </div>
        <ZeiterfassungOverview
          class="zeiterfassungOverview tile"
          :key="overviewKey"
          @refreshUi="refreshUis"
        />
      </div>
      <ZeiterfassungHistoryOverview
        class="zeiterfassungHistoryOverview tile"
        :key="zeiterfassungHistoryOverviewKey"
      />
    </div>
  </div>
</template>

<script>
import ZeiterfassungDashboard from './views/ZeiterfassungDashboard.vue';
import ZeiterfassungRecordingStatus from './views/ZeiterfassungRecordingStatus.vue';
import ZeiterfassungOverview from './views/ZeiterfassungOverview.vue';
import SetActualHoursComparison from './views/SetActualHoursComparison.vue';
import ZeiterfassungHistoryOverview from './views/ZeiterfassungHistoryOverview.vue';

export default {
  name: 'App',
  components: {
    ZeiterfassungDashboard,
    ZeiterfassungRecordingStatus,
    ZeiterfassungOverview,
    SetActualHoursComparison,
    ZeiterfassungHistoryOverview,
  },
  data () {
   return {
     overviewKey: 0,
     zeiterfassungStatusKey: 0,
     setActualHoursComparisonKey: 0,
     zeiterfassungHistoryOverviewKey: 0,
   };
 },
  methods: {
    refreshUis: function(){
      this.setActualHoursComparisonKey += 1;
      this.overviewKey += 1;
      this.zeiterfassungStatusKey += 1;
      console.log("App.vue: Refresh requested!");
    },
    refreshAllUisInclHistory: function(){
      this.setActualHoursComparisonKey += 1;
      this.overviewKey += 1;
      this.zeiterfassungStatusKey += 1;
      this.zeiterfassungHistoryOverviewKey += 1;
      console.log("App.vue: Refresh requested incl. history!");
    },
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
    color: white;
    table-layout:dynamic;
    border-collapse: collapse;
    text-align: left;
    padding: 5px;
  }

  td.fitwidth {
    width: 1px;
    white-space: nowrap;
  }

  th {
    padding: 5px;
    padding-right: 20px;
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

  .containerElement{
    width: 49.5%;
  }

  .center {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .centered {
    margin: auto;
  }

  .centeredText {
    text-align: center;
  }

  .timeRecordingStatusAndActualHoursComparison{
    padding-right: 15px;
    width: 20%;
    height: auto;
  }

  .statusAndBDayOverviewPanelAndActualHoursPanel{
    display: flex;
    width: 100%;
    padding-bottom: 15px;
  }

  .content{
    position: relative;
    width: auto;
    padding-left: 50px;
    padding-right: 50px;
    display: flexbox;
    flex-flow: row wrap;
  }

  .setActualHoursComparison{
    width: auto;
    height: auto;
    margin-bottom: 15px;
  }

  .zeiterfassungRecordingStatus{
    width: auto;
  }

  .zeiterfassungOverview{
    width: 80%;
  }

  .zeiterfassungHistoryOverview{
    width: auto;
  }

  .tile{
    padding: 5px;
    max-width:100%;
    border-style: outset;
    border-width: 3px;
    border-color: #358fe6;
  }
</style>
