export default {
  name: 'date-calculations',
  methods: {
    getNumberWithLeadingZero: function(number){
      if (number < 10){
        return "0" + number;
      }
      return number;
    },
    calculateDateFromDuration: function (beginTimeRepresentation, endTimeRepresentation, duration){
      var today = new Date().toISOString();
      var todayDateAsString = today.substring(0, today.indexOf('T') + 1);
      var newEndDate = new Date(todayDateAsString + beginTimeRepresentation);
      var beginDate = new Date(todayDateAsString + beginTimeRepresentation);
      var totalEndMinutes = beginDate.getMinutes() + (duration * 60);
      newEndDate.setMinutes(totalEndMinutes);
      console.log("Total hours '" + newEndDate.getHours() + ", total minutes '" + newEndDate.getMinutes() + " (total '" + totalEndMinutes + "')");

      var newHoursAsString = this.getNumberWithLeadingZero(newEndDate.getHours());
      var newMinutesAsString = this.getNumberWithLeadingZero(newEndDate.getMinutes());

      console.log("New end-time snippet: '" + newHoursAsString + ":" + newMinutesAsString + "'");
      return newHoursAsString + ":" + newMinutesAsString;
    },
    beginOrEndTimeStampChanged: function(beginTimeRepresentationIn, endTimeRepresentationIn, currentDuration){
      var beginTimeRepresentation = this.harmonizeTimeStampRepresentation(beginTimeRepresentationIn);
      var endTimeRepresentation = this.harmonizeTimeStampRepresentation(endTimeRepresentationIn);

      var today = new Date().toISOString();
      var substringOfToday = today.substring(0, today.indexOf('T') + 1);
      var beginDate = new Date(substringOfToday + beginTimeRepresentation);
      var endDate = new Date(substringOfToday + endTimeRepresentation);

      var totalMinutesEndTimeStamp = 60 * endDate.getHours() + endDate.getMinutes();
      var totalMinutesBeginTimeStamp = 60 * beginDate.getHours() + beginDate.getMinutes();
      var hoursInBetween0 = (totalMinutesEndTimeStamp - totalMinutesBeginTimeStamp) / 60;
      var hoursInBetween = Math.round((hoursInBetween0 + Number.EPSILON) * 100) / 100;

      console.log("Time between changed begin and end time stamp: [" + hoursInBetween + " h]");
      if (isNaN(hoursInBetween)){
        console.log("Warning!'" + beginTimeRepresentation + "' & '" + endTimeRepresentation + "' could not be parsed!"
        + "Return current value '" + currentDuration + "' instead!");
        return currentDuration;
      }
      return hoursInBetween;
    },
    harmonizeTimeStampRepresentation: function(beginOrendTimeRepresentation){
      if (beginOrendTimeRepresentation == '0:00'){
        return '00:00';
      }
      return beginOrendTimeRepresentation;
    },
  }
}
