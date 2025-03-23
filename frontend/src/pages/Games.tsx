import { Key, useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../app/hooks"
import { fetchAllGames, selectAllGames } from "../features/games/gameSlice";
import { fetchTags, selectTags } from "../features/tags/tagSlice";
import { fetchCategories, selectCategories } from "../features/categories/categorySlice";
import { useNavigate } from "react-router-dom";

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
    <div className="p-4 text-white">
      <h1 className="text-3xl font-bold mb-6">Games</h1>

      <div className="mb-6 bg-gray-800 p-4 rounded-lg shadow-lg">
        <h2 className="text-xl font-semibold mb-3">Filters</h2>
        <div className="flex flex-col md:flex-row items-center gap-4 mb-4">

          <div>
            <label className="block mb-1 font-medium text-sm">Select Category:</label>
            <select
              className="w-52 bg-gray-700 text-white py-2 px-3 rounded-md outline-none"
              value={selectedCategory == null ? undefined : selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
            >
              <option value="">All Categories</option>
              {categories.map((cat: any) => (
                <option key={cat.name} value={cat.name}>
                  {cat.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block mb-1 font-medium text-sm">Select Tags:</label>
            <div className="flex flex-wrap gap-2">
              {tags.map((tag: any) => (
                <label
                  key={tag.id}
                  className="inline-flex items-center space-x-2 cursor-pointer"
                >
                  <input
                    type="checkbox"
                    className="accent-indigo-600 w-4 h-4"
                    checked={selectedTags.includes(tag.name)}
                    onChange={() => handleTagToggle(tag.name)}
                  />
                  <span className="text-sm">{tag.name}</span>
                </label>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Games Grid */}
      {filteredGames.length === 0 && (
        <p className="mt-4 text-center text-gray-300">
          No games found for the selected filters.
        </p>
      )}
      <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-8 px-12">
        {filteredGames.map((game: any, index: Key | null | undefined) => (
          <div
            key={index}
            onClick={() => navigate(`/games/${game.id}`)}
            className="relative flex flex-col bg-gray-800 rounded-lg shadow-lg cursor-pointer
                       hover:shadow-2xl hover:bg-gray-700 transition-shadow overflow-hidden"
          >
            <div className="w-full h-40 bg-gray-600 flex items-center justify-center">
              <img src="/assets/images/godot_icon.png" className='w-40 h-40 object-cover' alt="Godot icon" />
            </div>
            <div className="p-4 flex flex-col flex-grow">
              <h3 className="font-bold text-lg mb-2">{game.title}</h3>
              <p className="text-sm text-gray-300 mb-2">
                Category: {game.category?.name || "None"}
              </p>
              <div className="flex flex-wrap gap-1 mb-2">
                {game.tags?.map((t: any, index: Key | null | undefined) => (
                  <span
                    key={index}
                    className="text-xs bg-indigo-600/20 border border-indigo-400 text-indigo-200 
                               rounded-full px-2 py-1 whitespace-nowrap"
                  >
                    {t.name}
                  </span>
                ))}
              </div>
              <p className="text-xs text-gray-400 line-clamp-3">
                {game.description}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
