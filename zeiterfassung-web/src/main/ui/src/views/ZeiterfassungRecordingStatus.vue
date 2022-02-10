<template>
  <div>
    <h2 class="centeredText">{{ title }} </h2>
    <div class="centered">
      <div
        v-bind:class="{ isRecording: timeRecorderDto.applicationStatus === 'RECORDING',
        isComeAndGo: timeRecorderDto.applicationStatus === 'COME_AND_GO',
        isNotRecording: timeRecorderDto.applicationStatus === 'IDLE'
        }">
        <label>
          {{ timeRecorderDto.statusMsg }}
        </label>
      </div>
      <div class="button-container">
        <button class="container-element element-left" :disabled="isStartStopButtonDisabled"
                v-on:click="startStopRecordingAndRefresh">
          {{ timeRecorderDto.applicationStatus === 'RECORDING' ? 'Aufzeichnung beenden' : 'Aufzeichnung starten' }}
        </button>
        <button class="container-element element-right" :disabled="isBookButtonDisabled" v-on:click="bookAndRefresh">Abbuchen</button>
      </div>
    </div>
    <add-businessday-increment
      v-bind:initBeginTimeStampRepresentation="beginTimeStampRepresentation"
      v-bind:initEndTimeStampRepresentation="endTimeStampRepresentation"
      v-bind:initTicketNr="ticketNr"
      v-if="isAddBusinessDayIncrementActive"
      @refreshUi="refreshUiState"
      @resumed="onResumed"
    >
    </add-businessday-increment>
    <error-box v-if="postErrorDetails">
      {{ postErrorDetails }}
    </error-box>
  </div>
</template>

<script>
import AddNewBusinessDayIncrement from '../components/AddNewBusinessDayIncrement.vue';
import ErrorBox from '../components/ErrorBox.vue';
import timeRecorderApi from '../mixins/TimeRecorderApi';
import appApi from '../mixins/AppApi';

export default {
  name: 'ZeiterfassungRecordingStatus',
  mixins: [timeRecorderApi, appApi],
  components: {
    'add-businessday-increment': AddNewBusinessDayIncrement,
    'error-box': ErrorBox
  },
  data() {
    return {
      title: 'Aktueller Status',
      isAddBusinessDayIncrementActive: false,
      ticketNr: '',
      beginTimeStampRepresentation: '',
      endTimeStampRepresentation: '',
    }
  },
  computed: {
    isBookButtonDisabled: function () {
      return !this.timeRecorderDto.isBookingPossible;
    },
    isStartStopButtonDisabled: function () {
      return this.isAddBusinessDayIncrementActive
          ||!this.timeRecorderDto
          || this.timeRecorderDto.applicationStatus === 'COME_AND_GO';
    },
    timeRecorderDto: function () {
      return this.$store.getters.timeRecorderDto
    },
  },
  methods: {
    bookAndRefresh: function () {
      this.book();
      this.refreshUiWithHistory();
    },
    startStopRecordingAndRefresh: function () {
      this.startStopRecording();
      this.fetchTimeRecorderDto();// only 'refresh' this view
    },
    onResumed: function () {
      this.isAddBusinessDayIncrementActive = false;
      this.refreshUiState();
    },
    refreshUiState: function () {
      this.requestUiRefresh();
    },
  },
  mounted() {
    this.fetchTimeRecorderDto();
  }
}
</script>
<style scoped>

.button-container {
  display: flex;
  justify-content: center;
}

.isNotRecording {
  background-color: #ffcccb;
  margin-bottom: 5px;
  padding: 3px;
}

.isComeAndGo {
  background-color: #358fe6;
  margin-bottom: 5px;
  padding: 3px;
}

.isRecording {
  background-color: #90EE90;
  margin-bottom: 5px;
  padding: 3px;
}
</style>
