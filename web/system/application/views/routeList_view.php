<H2> My Games </H2>
<p>
	Games cannot be modified, but you can unpublish a game by clicking the green icon.<br />
	If you wish to unpublish a game click the red X.
</p>
<table id="myGames" cellpadding="5" cellspacing="0">
	<tr>
		<td class="myGameName"> Game name </td>
		<td class="paddedColumn"> Published </td>
	</tr>
<?
$i = 0;
if(isset($myGames['games'])) {
	foreach($myGames['games'] as $row) {
			echo "<tr class='highlightRow'>";
				echo "<td class='column1'>".$row->gameName."</td>";
				echo "<td class='paddedColumn'>";
				if($row->active) {
					echo anchor('game/deactivate/'.$row->gameId,'<img src="system/application/views/images/update.png" border="0" title="Unpublish game">');
					}
				else {
					echo anchor('game/activate/'.$row->gameId,'<img src="system/application/views/images/delete.png" border="0" title="Publish game">');
					}
				echo "</td>";
			echo "</tr>";
		}
	}
else
	echo "You haven't created any games yet.";	
?>
</table>

<?// echo $i; ?>