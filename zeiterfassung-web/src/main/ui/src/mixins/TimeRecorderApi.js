const timeRecorderApiUrl = "/api/v1/timerecorder";
export default {
  name: 'timeRecorderApi',
  data() {
    return {
      timeRecorderDto: '',
      ticketsFromBacklog: '',
      postErrorDetails: null,
      postRequestOptions: {
        method: "POST",
        headers: {"Content-Type": "application/json"}
      },
    }
  },
  methods: {
    fetchTimeRecorderDto: function () {
      fetch(timeRecorderApiUrl)
        .then(response => response.json())
        .then(data => this.timeRecorderDto = data);
    },

    startStopRecording: function () {
      fetch(timeRecorderApiUrl + "/startStopRecording", this.postRequestOptions)
        .then(async response => {
          // retrieve the response in two stepts: text -> JSON.parse. Thats necessary in case we receive an empty response (which leads always to an exception)
          const plainData = await response.text();
          const data = await (plainData ? JSON.parse(plainData) : {});
          // check for error response
          if (!response.ok) {
            // get error message from body or default to response status
            const error = (data && data.message) || response.status;
            this.postErrorDetails = data;
            return Promise.reject(error);
          }
          this.addNewBusinessDayIncrementDto = data;
          this.ticketNr = this.addNewBusinessDayIncrementDto.businessDayIncrementDto.ticketDto.ticketNr;
          this.beginTimeStampRepresentation = this.addNewBusinessDayIncrementDto.businessDayIncrementDto.timeSnippetDto.beginTimeStampRepresentation;
          this.endTimeStampRepresentation = this.addNewBusinessDayIncrementDto.businessDayIncrementDto.timeSnippetDto.endTimeStampRepresentation;
          this.isAddBusinessDayIncrementActive = this.beginTimeStampRepresentation !== null && this.endTimeStampRepresentation !== null;
          console.info("begin: " + this.beginTimeStampRepresentation + ", end: " + this.endTimeStampRepresentation + ", is active: " + this.isAddBusinessDayIncrementActive);
        }).catch(error => console.error("Error occurred while start/stop businessDayIncrement", error));
    },
    resume: function () {
      fetch(timeRecorderApiUrl + "/resume", this.postRequestOptions)
        .then(response => response.json())
        .then(data => (this.postId = data.id))
        .catch(error => console.error("Error occurred while resuming", error));
      this.$emit('resumed');
    },
    book: function () {
      fetch(timeRecorderApiUrl + "/book", this.postRequestOptions)
        .then(response => response.json())
        .then(data => (this.postId = data.id))
        .catch(error => console.error("Error occurred while booking", error));
    },
  }
}
