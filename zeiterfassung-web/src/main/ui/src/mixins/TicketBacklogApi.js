const ticketBacklogApiUrl = "/api/v1/ticketbacklog";
export default {
  name: 'TicketBacklogApi',
  data (){
    return {
      tickets: '',
      possibleServiceCodes: '',
    }
  },
  methods: {
    fetchServiceCodes: function(ticketNr){
      fetch(ticketBacklogApiUrl + '/fetchServiceCodes/' + ticketNr)
      .then(async response => {
          // retrieve the response in two stepts: text -> JSON.parse. Thats necessary in case we receive an empty response (which leads always to an exception)
          const plainData = await response.text();
          const data = await (plainData ? JSON.parse(plainData) : {});
          // check for error response
          if(!response.ok){
            // get error message from body or default to response status
            const error = (data && data.message) || response.status;
            return Promise.reject(error);
          } else {
            this.possibleServiceCodes = data;
          }
      }).catch(error => console.error("Error occurred while fetching seviceCodes", error));
    },
    fetchTicketsFromBacklog: function(){
      fetch(ticketBacklogApiUrl + '/fetchTicketsFromBacklog')
        .then(response => response.json())
        .then(data => {
          this.tickets = data;
      });
    },
  }
}
