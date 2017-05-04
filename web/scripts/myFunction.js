/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function markAllRows(a){
$("#"+a).find("input:checkbox:enabled").attr("checked","checked").parents("tr").addClass("marked");
return true}
function unMarkAllRows(a){
$("#"+a).find("input:checkbox:enabled").removeAttr("checked").parents("tr").removeClass("marked");
return true
}

//Mark unmark javascript?
//http://stackoverflow.com/questions/5489113/mark-unmark-javascript
//This should do both of the things u want:
function checkAll(obj) {
        var tab = document.getElementById("logs");
        var elems = tab.getElementsByTagName("input");
        var len = elems.length;

        for (var i = 0; i < len; i++) {
            if (elems[i].type == "checkbox") {
                if(elems[i].checked == true){
                    elems[i].checked = false;
                }
                else{
                    elems[i].checked = true;
                }
            }
        }

        if(obj.innerHTML == 'Mark') { obj.innerHTML = 'Unmark' }
        else { obj.innerHTML = 'Mark' }
    }
//html:    
//    <th width='2%'><a href="#" onclick="checkAll(this);">Mark</a></th>";