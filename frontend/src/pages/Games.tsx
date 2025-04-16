import { Key, useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../app/hooks"
import { fetchAllGames, selectAllGames } from "../features/games/gameSlice";
import { fetchTags, selectTags } from "../features/tags/tagSlice";
import { fetchCategories, selectCategories } from "../features/categories/categorySlice";
import { useNavigate } from "react-router-dom";
import { FaFilter } from "react-icons/fa";

type Props = {}

export default function Games({ }: Props) {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const tags = useAppSelector(selectTags);
  const categories = useAppSelector(selectCategories);
  const games = useAppSelector(selectAllGames);

  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);

  useEffect(() => {
    dispatch(fetchCategories());
    dispatch(fetchTags());
    dispatch(fetchAllGames());
  }, [dispatch]);

  const handleTagToggle = (tagName: string) => {
    setSelectedTags((prev) =>
      prev.includes(tagName)
        ? prev.filter((t) => t !== tagName)
        : [...prev, tagName]
    );
  };

  const filteredGames = games.filter((game) => {
    const matchesCategory = selectedCategory
      ? game.category && game.category.name === selectedCategory
      : true;

    const matchesTags =
      selectedTags.length > 0
        ? game.tags && game.tags.some((t: any) => selectedTags.includes(t.name))
        : true;

    return matchesCategory && matchesTags;
  });

  return (
    <div className="pt-20 text-white">

      <div className="flex pt-16 w-10/12 mx-auto">

        {/* Sidebar */}
        <div className="group absolute h-3/4 flex flex-col w-16 hover:w-72 lg:w-72 transition-all duration-300 border-none z-10 bg-slate-800 border-r border-slate-700 p-3 rounded-lg shadow-lg">
          <div className="flex flex-col space-y-6 overflow-y-auto overflow-x-hidden h-full">
            <div
              className="relative w-full flex items-center justify-between px-2 py-2 rounded-lg transition-colors">
              <div className="flex items-center space-x-3">
                <FaFilter className="w-5 h-5" />
                <span className='tracking-wide text-2xl font-bold truncate'>Filters</span>
              </div>
            </div>
            <div className="hidden group-hover:block lg:block px-2">
              <label className="block mb-2 font-medium">Select Category:</label>
              <select
                className="w-full bg-gray-700 text-white py-2 px-3 rounded-md outline-none"
                value={selectedCategory == null ? '' : selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value || null)}
              >
                <option value="">All Categories</option>
                {categories.map((cat: any) => (
                  <option key={cat.name} value={cat.name}>
                    {cat.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="hidden group-hover:block lg:block px-2">
              <label className="block mb-2 font-medium">Select Tags:</label>
              <div className="flex flex-col space-y-3">
                {tags.map((tag: any) => (
                  <div>
                    <input
                      id={`tag-${tag.id}`}
                      type="checkbox"
                      className="peer hidden"
                      checked={selectedTags.includes(tag.name)}
                      onChange={() => handleTagToggle(tag.name)}
                    />
                    <label
                      key={tag.id}
                      htmlFor={`tag-${tag.id}`}
                      className="text-xs bg-gray-600 border border-primary text-indigo-200 rounded-full px-2 py-1 w-fit cursor-pointer peer-checked:bg-indigo-600/20 peer-checked:border-[#4c9cdb] peer-checked:text-primary">

                      <span className="text-sm truncate">{tag.name}</span>
                    </label>
                  </div>

                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Games Grid */}
        <div className="flex-1 overflow-auto px-6 ml-20 lg:ml-72">
          <div className="max-w-5xl 2xl:max-w-7xl w-full mx-auto">
            <h2 className="text-4xl font-bold mb-6 px-2 pt-4">Games</h2>
            {filteredGames.length === 0 ? (
              <p className="mt-4 text-center text-gray-300">
                No games found for the selected filters.
              </p>
            ) : (
              <div className="grid sm:grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8 px-2">
                {filteredGames.map((game: any, index: Key | null | undefined) => (
                  <div
                    key={game.id || index}
                    onClick={() => navigate(`/games/${game.id}`)}
                    className="w-full max-w-80 relative flex flex-col bg-gray-800 rounded-lg shadow-lg cursor-pointer
                             hover:shadow-2xl hover:bg-gray-700 transition-all duration-300 overflow-hidden"
                  >
                    <div className="w-full h-40 bg-gray-600 flex items-center justify-center overflow-hidden">
                      <img
                        src={game.imageUrl || "/assets/images/godot_icon.png"}
                        className='h-full object-cover'
                        alt={game.title || "Game image"}
                      />
                    </div>
                    <div className="p-4 flex flex-col justify-between flex-grow gap-4">
                      <div className="flex flex-col">
                        <h3 className="font-bold text-lg truncate">{game.title}</h3>
                        <p className="text-sm text-gray-300">
                          Category: {game.category?.name || "None"}
                        </p>
                      </div>
                      <div className="flex flex-wrap gap-1">
                        {game.tags?.map((t: any, tagIndex: Key | null | undefined) => (
                          <span
                            key={t.id || tagIndex}
                            className="text-xs bg-indigo-600/20 border border-indigo-400 text-indigo-200
                                     rounded-full px-2 py-1 whitespace-nowrap"
                          >
                            {t.name}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div >

    </div>
  );
}
