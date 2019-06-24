//————————————————————
// Description:
//   MultiCheckElem is the "custom_element" function that builds the custom multiple check box input
//   element. From what I have gathered, jqGrid calls this the first time the form is launched. After
//   that, only the "custom_value" function is called.
//
//   The full list of checkboxes is in the jqGrid "editoptions" section "list" tag (in the options
//   parameter).
//————————————————————
function MultiCheckElem( value, options )
{
   //———-
   // for each checkbox in the list
   //   build the input element
   //   set the initial "checked" status
   // endfor
   //———-
   var ctl = '';
   var ckboxAry = options.list.split(',');
   var op=value.split(',');
   ctl = "<div>";
   
   for (var i = 0 ; i < ckboxAry.length ; i++ )
   {
      var item = ckboxAry[i];
      var optins = item.split(":");
      ctl += '<input type="checkbox" ' + 'name="' + options.name+'"';
      for(var j = 0 ; j < op.length ; j++ ){
    	  if ( op[j] == optins[1] )
    	         ctl += 'checked="checked" ';
      }
      
      ctl += 'value="' + optins[0] + '"> ' + optins[1] + '</input>';
   }
   ctl += "</div>";
//   ctl = ctl.replace( /<br />&nbsp;$/, '' );
   return ctl;
}



//————————————————————
// Description:
//   MultiCheckVal is the "custom_value" function for the custom multiple check box input element. It
//   appears that jqGrid invokes this function the first time the form is submitted and, the rest of
//   the time, when the form is launched (action = set) and when it is submitted (action = 'get').
//————————————————————
function MultiCheckVal(elem, action, val)
{
   var items = '';
   if (action == 'get') // the form has been submitted
   {
      //———-
      // for each input element
      //   if it's checked, add it to the list of items
      // endfor
      //———-
	  elem = elem[0].children;
      for (var i = 0 ; i < elem.length ; i++)
      {
         if (elem[i].tagName == 'INPUT' && elem[i].checked )
            items += elem[i].value + ',';
      }

      // items contains a comma delimited list that is returned as the result of the element
      items = items.replace(/,$/, '');
   }
   else // the form is launched
   {
      //———-
      // for each input element
      //   based on the input value, set the checked status
      // endfor
      //———-
	  //elem = elem[0].children;
      for (var i in elem)
      {
         if (elem[i].tagName == 'INPUT')
         {
            if (val.indexOf(elem[i].value + '|') == -1)
               elem[i].checked = false;
            else
               elem[i].checked = true;
         }
      } // endfor
   }

   return items;
}