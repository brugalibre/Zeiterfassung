<template>
  <div id="app" class="centered">
    <ZeiterfassungDashboard/>
    <div class="content">
      <div class="timeRecordingDetailsLeftAndRightSide">
        <div class="timeRecordingDetailsLeftSide">
          <SetActualHoursComparison
            :key="setActualHoursComparisonKey"
            class="setActualHoursComparison tile"
            @refreshUi="refreshUis"
          />
          <ZeiterfassungRecordingStatus
            :key="zeiterfassungStatusKey"
            class="zeiterfassungRecordingStatus tile"
            @refreshUi="refreshUis"
            @refreshUiWithHistory="refreshAllUisInclHistory"
          />
        </div>
        <div class="timeRecordingDetailsRightSide">
          <ZeiterfassungOverview
            :key="overviewKey"
            class="zeiterfassungOverview tile"
            @refreshUi="refreshUis"
          />
        </div>
      </div>
      <div class="timeRecordingDetailsLeftAndRightSide">
        <div class="timeRecordingDetailsLeftSide">
          <TicketDistribution
            :key="ticketDistributionOverviewKey"
            class="ticketDistributionOverview tile"
            @refreshUiWithHistory="refreshAllUisInclHistory"
          />
        </div>
        <div class="timeRecordingDetailsRightSide">
          <ZeiterfassungHistoryOverview
            :key="zeiterfassungHistoryOverviewKey"
            class="zeiterfassungHistoryOverview tile"
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
  data() {
    return {
      applicationTitle: 'Zeiterfassung',
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
    refreshUis: function () {
      this.setActualHoursComparisonKey += 1;
      this.overviewKey += 1;
      this.zeiterfassungStatusKey += 1;
      this.ticketDistributionOverviewKey += 1;
      console.log("App.vue: Refresh requested!");
    },
    refreshAllUisInclHistory: function () {
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

  font-family: Verdana, Tahoma, Geneva, sans-serif;
}

button {
  background-color: #004587;
  color: white;
  table-layout: auto;
  word-wrap: break-word;
}

h1, h2, h3, label {
  word-wrap: break-word;
}

button:disabled {
  border-collapse: collapse;
  background-color: #9F9F9F;
}

table, th, td {
  color: black;
  table-layout: auto;
  border-collapse: collapse;
  text-align: left;
  padding: 5px;
  white-space: nowrap;
}

td {
  border-bottom: #004587 thin solid;
}

th {
  color: white;
  padding: 0.5vw 2vh;
  background-color: #004587;
}

th:first-child{
  border-top-left-radius: 7px;
}

th:last-child{
  border-top-right-radius: 7px;
}

.container {
  display: flex;
  justify-content: space-between;
  max-width: 400px;
  margin: 5px auto;
  padding: 0 0 0 0;
}

.container-element {
  width: 49.5%;
}

.element-left {
  margin-right: 5px;
}

.element-right {
  margin-left: 5px;
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

.timeRecordingDetailsLeftAndRightSide {
  display: flex;
  width: 100%;
}

.content {
  position: relative;
  width: auto;
  padding-left: 50px;
  padding-right: 50px;
  display: flex;
  flex-flow: row wrap;
}

.setActualHoursComparison {
  width: auto;
  height: auto;
  margin-bottom: 15px;
}

.ticketDistributionOverview {
  flex-grow: 2;
}

.zeiterfassungRecordingStatus {
  width: auto;
  margin-bottom: 15px;
  flex-grow: 2;
}

.zeiterfassungOverview {
  margin-bottom: 15px;
  width: auto;
  flex-grow: 2;
}

.zeiterfassungHistoryOverview {
  width: auto;
  flex-grow: 1;
}

.tile {
  padding: 10px;
  max-width: 100%;
  box-shadow: inset 0 3px 6px rgba(0,0,0,0.16), 0 4px 6px rgba(0,0,0,0.45);
  border-radius: 10px;
}
</style>
