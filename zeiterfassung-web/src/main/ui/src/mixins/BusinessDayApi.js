const businessDayApiUrl = "/api/v1/businessday";
export default {
  name: 'BusinessDayApi',
  data() {
    return {
      postErrorDetails: null,
      deleteRequestOptions: {
        method: "DELETE",
        headers: {"Content-Type": "application/json"}
      },
      postRequestOptions: {
        method: "POST",
        headers: {"Content-Type": "application/json"}
      },
    }
  },
  methods: {
    fetchBusinessDayDto: function () {
      fetch(businessDayApiUrl)
        .then(response => response.json())
        .then(data => this.$store.dispatch('setBusinessDay', data))
        .catch(error => console.error("Error occurred while fetching businessDay", error));
    },
    addBusinessDayIncrement: function (businessDayIncrement) {
      const requestOptions = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
          description: businessDayIncrement.description,
          serviceCodeDto: {
            value: businessDayIncrement.serviceCodeDto.value,
            representation: businessDayIncrement.serviceCodeDto.representation,
          },
          ticketDto: {
            ticketNr: businessDayIncrement.ticketNr,
          },
          timeSnippetDto: {
            beginTimeStampRepresentation: businessDayIncrement.beginTimeValue.timeRepresentation,
            endTimeStampRepresentation: businessDayIncrement.endTimeValue.timeRepresentation
          },
        })
      };

      // Call finally the api in order to change the businessday-increment
      fetch(businessDayApiUrl, requestOptions)
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
          } else {
            return data;
          }
        }).catch(error => console.error("Error occurred while adding a new businessDayIncrement", error));
    },
    changeBusinessDayIncrement: function (businessDayIncrement) {
      const requestOptions = {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
          id: businessDayIncrement.id,
          description: businessDayIncrement.description,
          serviceCodeDto: {
            value: businessDayIncrement.serviceCodeDto.value,
            representation: businessDayIncrement.serviceCodeDto.representation,
          },
          isBooked: false,
          totalDurationRep: businessDayIncrement.totalDurationRep,
          ticketDto: {
            ticketNr: businessDayIncrement.ticketDto.ticketNr,
            bookable: businessDayIncrement.ticketDto.bookable
          },
          timeSnippetDto: {
            id: businessDayIncrement.timeSnippetDto.id,
            durationRep: businessDayIncrement.timeSnippetDto.durationRep,
            beginTimeStampRepresentation: businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation,
            endTimeStampRepresentation: businessDayIncrement.timeSnippetDto.endTimeStampRepresentation
          },
        })
      };
      this.postErrorDetails = null;
      // Call finally the api in order to change the businessday-increment
      fetch(businessDayApiUrl, requestOptions)
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
        }).catch(error => console.error("Error occurred while changing businessDayIncrement'" + businessDayIncrement + "'", error));
    },
    deleteBusinessDayIncrement: function (businessDayIncrement) {
      fetch(businessDayApiUrl + "/" + businessDayIncrement.id, this.deleteRequestOptions)
        .then(response => response.json())
        .then(data => (this.postId = data.id))
        .catch(error => {
          console.error("Error occurred during deleting businessDayIncrement'" + businessDayIncrement + "'", error);
        });
    },
    deleteAll: function () {
      fetch(businessDayApiUrl + "/deleteAll", this.deleteRequestOptions)
        .then(response => response.json())
        .then(data => (this.postId = data.id))
        .catch(error => {
          console.error("Error occurred during deleting all businessDayIncrements", error);
        });
    },
  }
}
