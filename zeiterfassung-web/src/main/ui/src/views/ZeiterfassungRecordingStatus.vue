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
      @resumed="refreshUiState">
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
  export default {
    name: 'ZeiterfassungRecordingStatus',
    mixins: [timeRecorderApi],
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
        // call the actual api for booking and reload ui afterwards
        this.book();
        this.$emit('refreshUi', false);
      },
      startStopRecordingAndRefresh: function(){
        // call the actual api for start/stop and reload ui afterwards
        this.startStopRecording();
        this.fetchTimeRecorderDto();
      },
      refreshUiState: function(){
        console.log("ZeiterfassungRecordStatus: refreshUiStates");
        this.$emit('refreshUi', false);
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

  .isBookedRecord{
    background-color:lightslategray
  }

  .isNotRecording{
    background-color: #ffcccb;
  }

  .isRecording{
    background-color: #90EE90;
  }

</style>
