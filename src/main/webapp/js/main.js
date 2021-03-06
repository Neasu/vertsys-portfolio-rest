$(document).ready(() => {
    //Use enter for submitting
    $(document).bind('keypress', function(e) {
        if(e.keyCode==13){
            $('#searchSubmit').trigger('click');
        }
    });

    $('#searchSubmit').on('click', () => {
        const urlSearch = 'http://localhost:8080/search';
        const urlDetails = 'http://localhost:8080/detail/';

        var searchTerm = $('#searchTerm').val();
        var searchType = $('#searchType').val();
        var urlSearchThis = urlSearch + '?query=' + searchTerm + '&type=' + searchType;
        console.log('url:' + urlSearchThis);

        $('.progress').removeClass('hide');

        //clear table
        $('#resultList').empty();
        $('.searchFor').text(searchTerm);

        $.getJSON(urlSearchThis, (response) => {
            $('#resultList').on('click', '#infoButton', event=> {

                var spotifyId = $(event.currentTarget).data('spotifyid');
                var typeId = $(event.currentTarget).data('typeid');
                var urlDetailsThis = urlDetails + spotifyId + '?type=' + typeId;
                console.log('url:' + urlDetailsThis);



                $.getJSON(urlDetailsThis, (responseDetail) => {

                    //alert(responseDetail.title + ':\n' + responseDetail.info);

                    $('#insertInfo').html('Titel: ' + responseDetail.title + '<br>' + 'Infos: ' + responseDetail.info);
                    $('.modal').modal();
                    $('#modal1').modal('open');

                });
            });

            $('#resultList').on('click', '#playButton', event=> {
                $('#spotify-player').attr("src", "https://open.spotify.com/embed?uri=" + $(event.currentTarget).data('spotifylink'));
            });

            //append row for each item in response array
            $('#resultList').append(
                $.map(response.results, (item) => {
            	    return '<tr>'
                        + '<td>'
                        + '<img class="album-icon" src="' + item.imgUrl + '"></img>'
                        + '</td><td>'
                	    + item.title
                	    + '</td><td>'
                	    + item.description
            	        + '</td><td>'
                	    + '<a class="waves-effect waves-light btn" data-typeid="' + searchType + '"data-spotifyid="' + item.id + '" id="infoButton"><i class="material-icons left">info</i>more</a>&nbsp;'
            	        + '<a class="waves-effect waves-light btn" data-spotifylink="' + item.playLink + '" id="playButton"><i class="material-icons left">play_circle_filled</i>play</a>'
            	        + '</td></tr>';
            }).join());
        }).fail((e) => {
            //alert('An error occured')
            Materialize.toast("Bitte alle Felder ausfüllen",2000);
            console.log(e);
        }).always(() => {
            $('.progress').addClass('hide');
        });



    });

})