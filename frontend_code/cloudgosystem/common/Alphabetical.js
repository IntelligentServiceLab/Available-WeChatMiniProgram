const order = function(list) {
	list.sort((a, b) => a.name.localeCompare(b.name));
	return list;
}

export default {
	order
}