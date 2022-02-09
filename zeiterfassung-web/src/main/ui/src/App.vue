<template>
  <div id="app" class="centered" >
    <ZeiterfassungDashboard/>
    <div class="content">
      <div class="timeRecordingDetailsLeftAndRightSide">
        <div class="timeRecordingDetailsLeftSide">
          <SetActualHoursComparison
            class="setActualHoursComparison tile"
            :key="setActualHoursComparisonKey"
            @refreshUi="refreshUis"
          />
          <ZeiterfassungRecordingStatus
            class="zeiterfassungRecordingStatus tile"
            :key="zeiterfassungStatusKey"
            @refreshUi="refreshUis"
            @refreshUiWithHistory="refreshAllUisInclHistory"
          />
          <TicketDistribution
            class="ticketDistributionOverview tile"
            :key="ticketDistributionOverviewKey"
            @refreshUiWithHistory="refreshAllUisInclHistory"
          />
        </div>
        <div class="timeRecordingDetailsRightSide">
          <ZeiterfassungOverview
            class="zeiterfassungOverview tile"
            :key="overviewKey"
            @refreshUi="refreshUis"
          />
          <ZeiterfassungHistoryOverview
            class="zeiterfassungHistoryOverview tile"
            :key="zeiterfassungHistoryOverviewKey"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ZeiterfassungDashboard from './views/ZeiterfassungDashboard.vue';
import ZeiterfassungRecordingStatus from './views/ZeiterfassungRecordingStatus.vue';
import ZeiterfassungOverview from './views/ZeiterfassungOverview.vue';
import SetActualHoursComparison from './views/SetActualHoursComparison.vue';
import ZeiterfassungHistoryOverview from './views/ZeiterfassungHistoryOverview.vue';
import appApi from "./mixins/AppApi";
import TicketDistribution from "./views/TicketDistribution";

export default {
  name: 'App',
  mixins: [appApi],
  components: {
    TicketDistribution,
    ZeiterfassungDashboard,
    ZeiterfassungRecordingStatus,
    ZeiterfassungOverview,
    SetActualHoursComparison,
    ZeiterfassungHistoryOverview,
  },
  data () {
   return {
     applicationTitle: '',
     overviewKey: 0,
     ticketDistributionOverviewKey: 0,
     zeiterfassungStatusKey: 0,
     setActualHoursComparisonKey: 0,
     zeiterfassungHistoryOverviewKey: 0,
   };
 },
  watch: {
    applicationTitle: {
      immediate: true,
      handler() {
        document.title = this.applicationTitle;
      }
    }
  },
  methods: {
    refreshUis: function(){
      this.setActualHoursComparisonKey += 1;
      this.overviewKey += 1;
      this.zeiterfassungStatusKey += 1;
      this.ticketDistributionOverviewKey += 1;
      console.log("App.vue: Refresh requested!");
    },
    refreshAllUisInclHistory: function(){
      this.refreshUis();
      this.zeiterfassungHistoryOverviewKey += 1;
      console.log("App.vue: Refresh requested incl. history!");
    }
  },
  created() {
    this.applicationTitle = this.fetchApplicationTitle();
  }
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
    color: black;
    table-layout: dynamic;
    border-collapse: collapse;
    text-align: left;
    padding: 5px;
  }

  table td {
    border-top: #004587 thin solid;
    border-bottom: #004587 thin solid;
  }


  td.fitwidth {
    width: 1px;
    white-space: nowrap;
  }

  th {
    color: white;
    padding: 5px;
    padding-right: 20px;
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

  .timeRecordingDetailsLeftSide {
    padding-right: 15px;
    width: 20%;
    display: flex;
    flex-direction: column;
    height: auto;
  }

  .timeRecordingDetailsRightSide {
    display: flex;
    flex-direction: column;
    height: auto;
    width: 80%;
  }

  .timeRecordingDetailsLeftAndRightSide{
    display: flex;
    width: 100%;
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

  .ticketDistributionOverview {
    flex-grow: 12;
  }

  .zeiterfassungRecordingStatus{
    width: auto;
    margin-bottom: 15px;
  }

  .zeiterfassungOverview{
    margin-bottom: 15px;
    width: auto;
  }

  .zeiterfassungHistoryOverview {
    width: auto;
    flex-grow: 1;
  }

  .tile{
    padding: 5px;
    max-width:100%;
    border-style: outset;
    border-width: 3px;
    border-color: #358fe6;
  }
</style>
