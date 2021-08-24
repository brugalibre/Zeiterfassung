<template>
  <div>
    <h2>{{ title }} </h2>
    <div class="timeRecorderStatusView">
      <div v-bind:class="{ isRecording: timeRecorderDto.isRecording, isNotRecording: !timeRecorderDto.isRecording}" >{{timeRecorderDto.statusMsg}}</div>
      <div class="container">
        <button class="containerElement" :disabled="isAddBusinessDayIncrementActive" v-on:click="startStopRecordingAndRefresh">{{timeRecorderDto.isRecording ? 'Aufzeichnung beenden' : 'Aufzeichnung starten' }}</button>
        <button class="containerElement" :disabled="isBookButtonDisabled" v-on:click="bookAndRefresh">Abbuchen</button>
      </div>
    </div>
    <add-businessday-increment
      v-bind:initBeginTimeStampRepresentation="beginTimeStampRepresentation"
      v-bind:initEndTimeStampRepresentation="endTimeStampRepresentation"
      v-if="isAddBusinessDayIncrementActive"
      @businessDayIncrementAdded="refreshUiState"
      @resumed="refreshUiState"
    >
    </add-businessday-increment>
    <error-box v-if="postErrorDetails">
      {{postErrorDetails}}
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
        title: 'Aktueller Status:',
        isAddBusinessDayIncrementActive: false,
        beginTimeStampRepresentation: '',
        endTimeStampRepresentation: '',
      }
    },
    computed: {
      isBookButtonDisabled: function(){
        return !this.timeRecorderDto.isBookingPossible;
      }
    },
    methods: {
      bookAndRefresh: function(){
        this.book();
        this.requestUiRefresh();
      },
      startStopRecordingAndRefresh: function(){
        this.startStopRecording();
        this.fetchTimeRecorderDto();// only 'refresh' this view
      },
      refreshUiState: function(){
        console.log("ZeiterfassungRecordStatus: refreshUiStates");
        this.requestUiRefresh();
      },
  },
  mounted() {
    this.fetchTimeRecorderDto();
  }
}
</script>
<style scoped>
  .deleteAllButton{
    margin-right: 1em
  }

  .isNotRecording{
    background-color: #ffcccb;
  }

  .isRecording{
    background-color: #90EE90;
  }
</style>
